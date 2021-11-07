package me.hexalite.liteware.protocol.datatypes

import io.ktor.utils.io.core.*

fun ByteReadPacket.readBoolean(): Boolean {
    return readByte() == 1.toByte()
}

@OptIn(ExperimentalIoApi::class)
fun BytePacketBuilder.writeBoolean(boolean: Boolean) {
    writeByte(if(boolean) 1 else 0)
}
