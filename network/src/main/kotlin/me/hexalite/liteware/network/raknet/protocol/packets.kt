@file:JvmName("RakNetPackets")

package me.hexalite.liteware.network.raknet.protocol

import io.ktor.network.sockets.*
import io.ktor.utils.io.core.*
import me.hexalite.liteware.network.LitewareRakNetServer
import me.hexalite.liteware.network.annotations.RakNetPacketInfo
import me.hexalite.liteware.network.codec.RakNetPacketCodec
import me.hexalite.liteware.network.codec.encode
import me.hexalite.liteware.network.datatypes.RakNetReliability
import me.hexalite.liteware.network.raknet.protocol.connection.OpenConnectionReplyOne
import me.hexalite.liteware.network.raknet.protocol.connection.OpenConnectionReplyTwo
import me.hexalite.liteware.network.raknet.protocol.connection.OpenConnectionRequestOne
import me.hexalite.liteware.network.raknet.protocol.connection.OpenConnectionRequestTwo
import me.hexalite.liteware.network.raknet.protocol.custom.EncapsulatedPacket
import me.hexalite.liteware.network.raknet.protocol.custom.FrameSet
import me.hexalite.liteware.network.raknet.protocol.custom.UnknownPacket
import me.hexalite.liteware.network.raknet.protocol.custom.splitPacketFrom
import me.hexalite.liteware.network.raknet.protocol.kick.ConnectionBanned
import me.hexalite.liteware.network.raknet.protocol.kick.DisconnectNotification
import me.hexalite.liteware.network.raknet.protocol.kick.IncompatibleProtocolVersion
import me.hexalite.liteware.network.raknet.protocol.login.ConnectionRequest
import me.hexalite.liteware.network.raknet.protocol.login.ConnectionRequestAccepted
import me.hexalite.liteware.network.raknet.protocol.login.NewIncomingConnection
import me.hexalite.liteware.network.raknet.protocol.ping.ConnectedPing
import me.hexalite.liteware.network.raknet.protocol.ping.ConnectedPong
import me.hexalite.liteware.network.raknet.protocol.ping.UnconnectedPing
import me.hexalite.liteware.network.raknet.protocol.ping.UnconnectedPong
import me.hexalite.liteware.network.session.adjustedMtu
import me.hexalite.liteware.network.session.sessions
import kotlin.reflect.KClass
import kotlin.reflect.full.companionObjectInstance
import kotlin.reflect.full.findAnnotation


fun <T : RakNetPacket> findRakNetPacketId(clazz: KClass<T>): Byte =
    (clazz.findAnnotation<RakNetPacketInfo>()?.id ?: 0x80.toByte())

inline fun <reified T : RakNetPacket> findRakNetPacketId() = findRakNetPacketId(T::class)

fun findRakNetPacketCodec(id: Byte) = when (id) {
    findRakNetPacketId(UnconnectedPing::class) -> UnconnectedPing.Codec
    findRakNetPacketId(UnconnectedPong::class) -> UnconnectedPong.Codec
    findRakNetPacketId(ConnectedPing::class) -> ConnectedPing.Codec
    findRakNetPacketId(ConnectedPong::class) -> ConnectedPong.Codec
    findRakNetPacketId(OpenConnectionRequestOne::class) -> OpenConnectionRequestOne.Codec
    findRakNetPacketId(OpenConnectionRequestTwo::class) -> OpenConnectionRequestTwo.Codec
    findRakNetPacketId(OpenConnectionReplyOne::class) -> OpenConnectionReplyOne.Codec
    findRakNetPacketId(OpenConnectionReplyTwo::class) -> OpenConnectionReplyTwo.Codec
    findRakNetPacketId(ConnectionRequest::class) -> ConnectionRequest.Codec
    findRakNetPacketId(ConnectionRequestAccepted::class) -> ConnectionRequestAccepted.Codec
    findRakNetPacketId(NewIncomingConnection::class) -> NewIncomingConnection.Codec
    findRakNetPacketId(ConnectedPing::class) -> ConnectedPing.Codec
    findRakNetPacketId(ConnectionBanned::class) -> ConnectionBanned.Codec
    findRakNetPacketId(DisconnectNotification::class) -> DisconnectNotification.Codec
    findRakNetPacketId(IncompatibleProtocolVersion::class) -> IncompatibleProtocolVersion.Codec
    else -> if (FrameSet.IDs.any { it == id }) FrameSet.Codec else UnknownPacket.Codec
}

@OptIn(ExperimentalIoApi::class)
suspend fun <T : RakNetPacket> T.encodeToBytePacketBuilder(codec: RakNetPacketCodec<T>) = buildPacket {
    writeByte(findRakNetPacketId(this@encodeToBytePacketBuilder::class))
    codec.encode(this, this@encodeToBytePacketBuilder, this@encodeToBytePacketBuilder.details)
}

@OptIn(ExperimentalIoApi::class)
suspend fun <T : RakNetPacket> T.encodeToBytePacketBuilder() = buildPacket {
    val id = findRakNetPacketId(this@encodeToBytePacketBuilder::class)
    writeByte(id)
    (findRakNetPacketCodec(id) as RakNetPacketCodec<T>)
        .encode(this, this@encodeToBytePacketBuilder, this@encodeToBytePacketBuilder.details)
}

suspend fun <T : RakNetPacket> LitewareRakNetServer.send(packet: T, codec: RakNetPacketCodec<T>) =
    udp.ktor.outgoing.send(Datagram(packet.encodeToBytePacketBuilder(codec), packet.details.clientAddress))

suspend inline fun <reified T : RakNetPacket> LitewareRakNetServer.send(packet: T) =
    send(packet, packet::class.companionObjectInstance as RakNetPacketCodec<T>)


fun RakNetReliability.makeReliable() = when (this) {
    RakNetReliability.UNRELIABLE -> RakNetReliability.RELIABLE
    RakNetReliability.UNRELIABLE_SEQUENCED -> RakNetReliability.RELIABLE_SEQUENCED
    RakNetReliability.UNRELIABLE_WITH_ACK_RECEIPT -> RakNetReliability.UNRELIABLE_WITH_ACK_RECEIPT
    else -> this
}

private val orderedWriteIndex = IntArray(32)
private val highestSequencedWriteIndex = IntArray(32)

suspend fun <T : RakNetPacket> LitewareRakNetServer.sendFramed(
    packet: T,
    reliability: RakNetReliability,
    orderingChannel: Int,
    codec: RakNetPacketCodec<T>
) {
    val session = sessions.find(packet.details.clientAddress) ?: return
    val maximumSize = session.adjustedMtu
    val data = packet.encodeToBytePacketBuilder()
    val reliability = if (data.remaining > maximumSize) reliability.makeReliable() else reliability
    val sequence = if (reliability.isSequenced) highestSequencedWriteIndex[orderingChannel]++ else 0

    val ordering =
        if (reliability.isOrdered)
            EncapsulatedPacket.Ordering(orderedWriteIndex[orderingChannel]++, orderingChannel)
        else {
            highestSequencedWriteIndex[orderingChannel] = 0
            EncapsulatedPacket.Ordering(orderedWriteIndex[orderingChannel]++, orderingChannel + 1)
        }

    val copy = EncapsulatedPacket(
        data = data,
        split = null,
        ordering = ordering,
        reliability = reliability,
        reliabilityIndex = reliability.ordinal,
        sequenceIndex = sequence,
        details = packet.details
    )

    if (data.remaining > maximumSize) {
        val packets = splitPacketFrom(copy, packet.details)
        val frameSet = FrameSet(0, packets, packet.details)
        send(frameSet, FrameSet.Codec)
    } else {
        send(FrameSet(0, listOf(copy), packet.details))
    }
}

suspend inline fun <reified T : RakNetPacket> LitewareRakNetServer.sendFramed(
    packet: T,
    reliability: RakNetReliability,
    orderingChannel: Int
) = sendFramed(packet, reliability, orderingChannel, packet::class.companionObjectInstance as RakNetPacketCodec<T>)
