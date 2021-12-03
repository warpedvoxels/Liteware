@file:JvmName("MinecraftPackets")

package me.hexalite.liteware.protocol.packet

import me.hexalite.liteware.protocol.packet.inbound.ClientToServerHandshakePacket
import me.hexalite.liteware.protocol.packet.inbound.LoginPacket
import me.hexalite.liteware.protocol.utils.id

fun Number.hex(): String {
    val hexString = Integer.toHexString(this.toInt() and 0xFF)
    return if (hexString.length % 2 != 0) "0x0${hexString.uppercase()}" else "0x${hexString.uppercase()}"
}

suspend fun findMinecraftPacketCodecOrNull(id: Int) = when (id) {
    ClientToServerHandshakePacket::class.id() -> ClientToServerHandshakePacket.Codec
    LoginPacket::class.id() -> LoginPacket.Codec
    else -> null
}

suspend fun findMinecraftPacketCodec(id: Int) = findMinecraftPacketCodecOrNull(id)
    ?: error("Unknown minecraft packet with id '${id.hex()}'")
