@file:JvmName("MinecraftPackets")

package me.hexalite.liteware.protocol.packet

import me.hexalite.liteware.protocol.packet.inbound.ClientToServerHandshakePacket
import me.hexalite.liteware.protocol.packet.inbound.LoginPacket
import me.hexalite.liteware.protocol.utils.id

suspend fun findMinecraftPacketCodec(id: Byte) = when (id) {
    ClientToServerHandshakePacket::class.id() -> ClientToServerHandshakePacket.Codec
    LoginPacket::class.id() -> LoginPacket.Codec
    else -> error("Unknown minecraft packet with id '$id'")
}

