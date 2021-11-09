package me.hexalite.liteware.network

import io.ktor.network.sockets.*
import io.ktor.utils.io.core.*
import me.hexalite.liteware.network.annotations.RakNetPacketInfo
import me.hexalite.liteware.network.packets.*
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

object RakNet {
    object Flags {
        const val VALID = 128.toByte()
        const val ACK = 64.toByte()
        const val HAS_B_AND_AS = 32.toByte()
        const val NACK = 32.toByte()
        const val PACKET_PAIR = 16.toByte()
        const val CONTINUOUS_SEND = 8.toByte()
        const val NEEDS_B_AND_AS = 4.toByte()
    }

    // The RakNet protocol version used by Mojang.
    const val RAKNET_PROTOCOL_VERSION: Short = 10

    const val MINIMUM_MTU_SIZE: Short = 576
    const val MAXIMUM_MTU_SIZE: Short = 1400

    const val MAXIMUM_ORDERING_CHANNELS = 16
    const val MAXIMUM_ENCAPSULATED_HEADER_SIZE = 28

    const val UDP_HEADER_SIZE = 8
    const val RAKNET_DATAGRAM_HEADER_SIZE = 4

    const val MAXIMUM_CONNECTION_ATTEMPTS = 10
    const val SESSION_TIMEOUT_MS = 30000
    const val SESSION_STALE_MS = 5000
    const val RAKNET_PING_INTERVAL = 1000
    const val MAXIMUM_STALE_DATAGRAMS = 256

    val UNCONNECTED_MAGIC = byteArrayOf(0, -1, -1, 0, -2, -2, -2, -2, -3, -3, -3, -3, 18, 52, 86, 120)

}

fun <T : RakNetPacket> findRakNetPacketId(clazz: KClass<T>): Byte = (clazz.findAnnotation<RakNetPacketInfo>()?.id ?: -1)

inline fun <reified T : RakNetPacket> findRakNetPacketId() = findRakNetPacketId(T::class)

fun findRakNetPacket(id: Byte, datagram: Datagram) = when (id) {
    findRakNetPacketId(ConnectedPing::class) -> ConnectedPing(datagram)
    findRakNetPacketId(UnconnectedPing::class) -> UnconnectedPing(datagram)
    findRakNetPacketId(UnconnectedPingOpenRequests::class) -> UnconnectedPingOpenRequests(datagram)
    findRakNetPacketId(ConnectedPong::class) -> ConnectedPong(datagram)
    findRakNetPacketId(DetectLostConnection::class) -> DetectLostConnection(datagram)
    findRakNetPacketId(OpenConnectionRequest1::class) -> OpenConnectionRequest1(datagram)
    findRakNetPacketId(OpenConnectionReply1::class) -> OpenConnectionReply1(datagram)
    findRakNetPacketId(OpenConnectionRequest2::class) -> OpenConnectionRequest2(datagram)
    findRakNetPacketId(OpenConnectionReply2::class) -> OpenConnectionReply2(datagram)
    findRakNetPacketId(ConnectionRequest::class) -> ConnectionRequest(datagram)
    findRakNetPacketId(ConnectionRequestAccepted::class) -> ConnectionRequestAccepted(datagram)
    findRakNetPacketId(ConnectionRequestFailed::class) -> ConnectionRequestFailed(datagram)
    findRakNetPacketId(AlreadyConnected::class) -> AlreadyConnected(datagram)
    findRakNetPacketId(NewIncomingConnection::class) -> NewIncomingConnection(datagram)
    findRakNetPacketId(NoFreeIncomingConnections::class) -> NoFreeIncomingConnections(datagram)
    findRakNetPacketId(ConnectionLost::class) -> ConnectionLost(datagram)
    findRakNetPacketId(ConnectionBanned::class) -> ConnectionBanned(datagram)
    findRakNetPacketId(IncompatibleProtocolVersion::class) -> IncompatibleProtocolVersion(datagram)
    findRakNetPacketId(UnconnectedPong::class) -> UnconnectedPong(datagram)
    else -> UnknownPacket(datagram)
}

fun unconnectedMagicOrNull(data: ByteReadPacket): ByteArray? {
    val magic = data.readBytes(RakNet.UNCONNECTED_MAGIC.size)
    return if (magic.contentEquals(RakNet.UNCONNECTED_MAGIC)) magic else null
}