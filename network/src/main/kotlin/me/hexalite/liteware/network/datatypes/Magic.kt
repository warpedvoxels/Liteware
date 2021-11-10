package me.hexalite.liteware.network.datatypes

import io.ktor.utils.io.core.*

object Magic {

    val value = byteArrayOf(0, -1, -1, 0, -2, -2, -2, -2, -3, -3, -3, -3, 18, 52, 86, 120)

    fun encodeTo(bytePacketBuilder: BytePacketBuilder) = bytePacketBuilder.writeFully(value)

    fun decodeFrom(byteReadPacket: ByteReadPacket): Magic? {
        val magic = byteReadPacket.readBytes(value.size)
        return if (magic.contentEquals(value)) Magic else null
    }

}

inline fun ByteReadPacket.readMagic(): Magic? = Magic.decodeFrom(this)

inline fun BytePacketBuilder.writeMagic() = Magic.encodeTo(this)
