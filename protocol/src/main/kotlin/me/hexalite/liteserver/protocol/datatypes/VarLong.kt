package me.hexalite.liteserver.protocol.datatypes

import io.ktor.utils.io.core.*
import kotlin.experimental.and

interface VarLong {
    val long: Long
}

@JvmInline
value class SignedVarLong(override val long: Long) : VarLong

@JvmInline
value class UnsignedVarLong(override val long: Long) : VarLong

fun VarLong(long: Long) = if (long < 0) SignedVarLong(long) else UnsignedVarLong(long)

@OptIn(ExperimentalIoApi::class)
fun BytePacketBuilder.encodeVarLong(varLong: VarLong) {
    var value = varLong.long
    while (true) {
        if ((value and -0x80) == 0L) {
            writeByte(value.toByte())
            return
        }
        writeByte(((value and 0x7F) or 0x80).toByte())
        value = value ushr 7
    }
}

fun ByteReadPacket.decodeVarLong(): VarLong {
    var value = 0L
    var offset = 0

    while (true) {
        val b = readByte()
        value = value or ((b and 0x7F).toLong().shl(offset * 7))
        if (offset > 10) {
            throw InvalidDataTypeException("Invalid (un)signed VarLong (offset must be in ).")
        }
        if ((b and 0x80.toByte()).toLong() == 0L) {
            return VarLong(value)
        }
        offset++
    }
}
