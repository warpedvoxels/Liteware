@file:JvmName("IntBigEndian")
package me.hexalite.liteware.protocol.datatypes

import io.ktor.utils.io.core.*

fun ByteReadPacket.readIntBigEndian(): Int {
    val b1 = readByte()
    val b2 = readByte()
    val b3 = readByte()
    val b4 = readByte()
    return (b1.toInt() shl 24) or (b2.toInt() shl 16) or (b3.toInt() shl 8) or b4.toInt()
}

@OptIn(ExperimentalIoApi::class)
fun BytePacketBuilder.writeIntBigEndian(integer: Int) {
    writeByte((integer shr 24).toByte())
    writeByte((integer shr 16).toByte())
    writeByte((integer shr 8).toByte())
    writeByte(integer.toByte())
}
