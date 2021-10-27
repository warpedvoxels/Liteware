package me.hexalite.software.protocol.datatypes

import io.ktor.utils.io.core.*
import kotlin.experimental.and

interface VarInt {
    val integer: Int
}

@JvmInline
value class SignedVarInt(override val integer: Int) : VarInt

@JvmInline
value class UnsignedVarInt(override val integer: Int) : VarInt

fun VarInt(int: Int): VarInt {
    if (int in -2147483648..2147483647) {
        return SignedVarInt(int)
    }
    if (int in 0..4294967295) {
        return UnsignedVarInt(int)
    }
    throw InvalidDataTypeException("Invalid VarInt (must be in -2147483648..2147483647 or 0..4294967295).")
}

@OptIn(ExperimentalIoApi::class)
fun BytePacketBuilder.encodeVarInt(varInt: VarInt) {
    var value = varInt.integer

    while (true) {
        if ((value and 0xFFFFFF80.toInt()) == 0) {
            writeByte(value.toByte())
            return
        }
        writeByte(((value and 0x7F) or 0x80).toByte())
        value = value ushr 7
    }
}

fun ByteReadPacket.decodeVarInt(): VarInt {
    var value = 0L
    var offset = 0
    var byte: Byte

    do {
        if (offset == 35) {
            throw InvalidDataTypeException("Invalid (un)signed VarInt (must be in -2147483648..2147483647).")
        }
        byte = readByte()
        value = value or ((byte.toLong() and 0x7FL) shl offset)
        offset += 7
    } while ((byte and 128.toByte()) != 0.toByte())

    return VarInt(value.toInt())
}
