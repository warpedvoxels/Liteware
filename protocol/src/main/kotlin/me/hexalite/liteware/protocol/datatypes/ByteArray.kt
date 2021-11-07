package me.hexalite.liteware.protocol.datatypes

import io.ktor.utils.io.core.*

fun ByteReadPacket.decodeMinecraftByteArray(): ByteArray {
    val length = decodeVarInt()
    return readBytes(length.integer)
}

fun BytePacketBuilder.encodeMinecraftByteArray(bytes: ByteArray) {
    encodeVarInt(VarInt(bytes.size))
    writeFully(bytes)
}