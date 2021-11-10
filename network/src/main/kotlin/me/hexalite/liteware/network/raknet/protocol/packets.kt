@file:JvmName("Packets")

package me.hexalite.liteware.network.raknet.protocol

import io.ktor.network.sockets.*
import io.ktor.utils.io.core.*
import me.hexalite.liteware.network.LitewareRakNetServer
import me.hexalite.liteware.network.annotations.RakNetPacketInfo
import me.hexalite.liteware.network.codec.RakNetPacketCodec
import me.hexalite.liteware.network.codec.decode
import me.hexalite.liteware.network.codec.encode
import me.hexalite.liteware.network.raknet.protocol.connection.OpenConnectionRequestTwo
import me.hexalite.liteware.network.raknet.protocol.custom.RakNetCustomPacket
import me.hexalite.liteware.network.raknet.protocol.login.ConnectionRequest
import me.hexalite.liteware.network.raknet.protocol.login.NewIncomingConnection
import me.hexalite.liteware.network.raknet.protocol.ping.ConnectedPing
import me.hexalite.liteware.network.raknet.protocol.ping.UnconnectedPing
import kotlin.reflect.KClass
import kotlin.reflect.full.companionObjectInstance
import kotlin.reflect.full.findAnnotation

fun <T : RakNetPacket> findRakNetPacketId(clazz: KClass<T>): Byte = (clazz.findAnnotation<RakNetPacketInfo>()?.id ?: -1)

inline fun <reified T : RakNetPacket> findRakNetPacketId() = findRakNetPacketId(T::class)

fun decodeRakNetPacket(id: Byte, datagram: Datagram, server: LitewareRakNetServer) = when (id) {
    findRakNetPacketId(ConnectedPing::class) ->
        ConnectedPing.Codec.decode(datagram.packet, RakNetPacketDetails(server, datagram.address))
    findRakNetPacketId(UnconnectedPing::class) ->
        UnconnectedPing.Codec.decode(datagram.packet, RakNetPacketDetails(server, datagram.address))
    findRakNetPacketId(OpenConnectionRequestTwo::class) ->
        OpenConnectionRequestTwo.Codec.decode(datagram.packet, RakNetPacketDetails(server, datagram.address))
    findRakNetPacketId(ConnectionRequest::class) ->
        ConnectionRequest.Codec.decode(datagram.packet, RakNetPacketDetails(server, datagram.address))
    findRakNetPacketId(NewIncomingConnection::class) ->
        NewIncomingConnection.Codec.decode(datagram.packet, RakNetPacketDetails(server, datagram.address))
    else -> RakNetCustomPacket.Codec.decode(datagram.packet, RakNetPacketDetails(server, datagram.address))
}

@OptIn(ExperimentalIoApi::class)
suspend fun <T : RakNetPacket> LitewareRakNetServer.send(packet: T, codec: RakNetPacketCodec<T>) =
    udp.ktor.send(Datagram(buildPacket {
        writeByte(findRakNetPacketId(packet::class))
        codec.encode(this, packet, packet.details)
    }, packet.details.clientAddress))

suspend inline fun <reified T : RakNetPacket> LitewareRakNetServer.send(packet: T) =
    send(packet, packet::class.companionObjectInstance as RakNetPacketCodec<T>)

