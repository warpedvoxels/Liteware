package me.hexalite.liteware.protocol.datatypes

import io.ktor.utils.io.core.*

fun ByteReadPacket.readMinecraftString(): String {
    val length = readVarInt()
    return readBytes(length.integer).decodeToString()
}

fun BytePacketBuilder.writeMinecraftString(string: String) {
    writeVarInt(VarInt(string.length))
    writeFully(string.encodeToByteArray())
}
