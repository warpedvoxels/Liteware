package me.hexalite.liteware.protocol.datatypes

import io.ktor.utils.io.core.*

fun ByteReadPacket.decodeMinecraftString(): String {
    val length = decodeVarInt()
    return readBytes(length.integer).decodeToString()
}

fun BytePacketBuilder.encodeMinecraftString(string: String) {
    encodeVarInt(VarInt(string.length))
    writeFully(string.encodeToByteArray())
}
