package me.hexalite.liteware.protocol.datatypes

import io.ktor.utils.io.core.*

fun ByteReadPacket.readMinecraftByteArray(): ByteArray {
    val length = readUVarInt()
    return readBytes(length.uint.toInt())
}

fun BytePacketBuilder.writeMinecraftByteArray(bytes: ByteArray) {
    writeUVarInt(UVarInt(bytes.size.toUInt()))
    writeFully(bytes)
}