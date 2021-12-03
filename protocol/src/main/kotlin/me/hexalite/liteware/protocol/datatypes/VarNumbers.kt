@file:JvmName("VarNumbers")

package me.hexalite.liteware.protocol.datatypes

import io.ktor.utils.io.core.*
import me.hexalite.liteware.protocol.exception.InvalidDataTypeException

@JvmInline
value class VarInt(val int: Int) {
    fun toInt() = int
}

@JvmInline
value class UVarInt(val uint: UInt) {
    fun toUInt() = uint

    fun toInt() = uint.toInt()

    fun toVarInt() = VarInt(toInt())
}

@JvmInline
value class VarLong(val long: Long) {
    fun toLong() = long
}

@JvmInline
value class UVarLong(val ulong: ULong) {
    fun toULong() = ulong

    fun toLong() = ulong.toLong()

    fun toVarLong() = VarLong(toLong())
}

fun BytePacketBuilder.writeVarInt(value: VarInt) = with(value.int.toLong()) {
    encode(((this shl 1) xor (this shr 31)) and 0xFFFFFFFFL)
}

fun BytePacketBuilder.writeUVarInt(value: UVarInt) = encode(value.uint.toLong() and 0xFFFFFFFFL)

fun ByteReadPacket.readVarInt(): VarInt = VarInt(decodeZigZag32(decode()).toInt())

fun ByteReadPacket.readUVarInt() = UVarInt(decode().toUInt())

fun BytePacketBuilder.writeVarLong(value: VarLong) = encode((value.toLong() shl 1) xor (value.toLong() shr 63))

fun BytePacketBuilder.writeUVarLong(value: UVarLong) = encode(value.toLong())

fun ByteReadPacket.readVarLong() = VarLong(decodeZigZag32(decode()))

fun ByteReadPacket.readUVarLong() = UVarLong(decode().toULong())

fun ByteReadPacket.decodeZigZag32(long: Long) = (long ushr 1 xor -(long and 1))

@OptIn(ExperimentalUnsignedTypes::class)
private fun ByteReadPacket.decode(): Long {
    var result = 0L
    var shift = 0
    while (shift < 64) {
        val value = readUByte().toLong()
        result = ((value and 0x7FL) shl shift) or result
        if ((value and 0x80) == 0L) {
            return result
        }
        shift += 7
    }
    throw InvalidDataTypeException("VarLong was wider than 70-bit or VarInt was wider than 35-bit.")
}

private fun BytePacketBuilder.encode(value: Long) {
    var value = value
    while (true) {
        if (value and 0x7FL.inv() == 0L) {
            writeByte(value.toByte())
            return
        } else {
            writeByte((value.toInt() and 0x7F or 0x80).toByte())
            value = value ushr 7
        }
    }
}