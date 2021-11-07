package me.hexalite.liteware.protocol.datatypes

import io.ktor.utils.io.core.*

fun ByteReadPacket.readMinecraftByteArray(): ByteArray {
    val length = readVarInt()
    return readBytes(length.integer)
}

fun BytePacketBuilder.writeMinecraftByteArray(bytes: ByteArray) {
    writeVarInt(VarInt(bytes.size))
    writeFully(bytes)
}