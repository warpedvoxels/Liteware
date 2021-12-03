package me.hexalite.liteware.protocol.datatypes

import io.ktor.utils.io.core.*

fun ByteReadPacket.readMinecraftString(): String {
    val length = readUVarInt()
    return readBytes(length.uint.toInt()).decodeToString()
}

fun BytePacketBuilder.writeMinecraftString(string: String) {
    writeUVarInt(UVarInt(string.length.toUInt()))
    writeFully(string.encodeToByteArray())
}
