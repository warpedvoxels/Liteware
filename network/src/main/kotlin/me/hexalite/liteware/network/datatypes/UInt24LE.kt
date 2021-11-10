@file:JvmName("UInt24LE")

package me.hexalite.liteware.network.datatypes

import io.ktor.utils.io.core.*

@OptIn(ExperimentalIoApi::class)
fun BytePacketBuilder.writeUInt24LE(value: Int) {
    writeByte(value.toByte())
    writeByte(value.ushr(8).toByte())
    writeByte(value.ushr(16).toByte())
}

fun ByteReadPacket.readUInt24LE(): Int =
    readByte().toInt() or
        (readByte().toInt() shl 8) or
        (readByte().toInt() shl 16)
