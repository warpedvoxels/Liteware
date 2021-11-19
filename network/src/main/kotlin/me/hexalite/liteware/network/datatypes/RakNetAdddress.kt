@file:JvmName("RakNetAddress")

package me.hexalite.liteware.network.datatypes

import io.ktor.utils.io.core.*
import me.hexalite.liteware.network.exceptions.DecodingException
import java.net.Inet4Address
import java.net.Inet6Address
import java.net.InetSocketAddress
import java.net.UnknownHostException

private val AF_INET6 = (if (System.getProperty("os.name") == "windows") 23 else 10).toUShort()

@OptIn(ExperimentalUnsignedTypes::class)
fun ByteReadPacket.readNetworkAddress(): InetSocketAddress {
    val version = readByte()
    return if (version.toInt() == 4) {
        val complement = readUInt().inv().toLong()
        val hostname = String.format(
            "%s.%s.%s.%s", (complement shr 24) and 0xFF,
            (complement shr 16) and 0xFF, (complement shr 8) and 0xFF,
            complement and 0xFF
        )
        val port = readUShort().toInt()
        InetSocketAddress.createUnresolved(hostname, port)
    } else {
        readUShort()
        val port = readUShort().toInt()
        readUInt()
        val address = readBytes(16)
        readUInt()
        try {
            InetSocketAddress(Inet6Address.getByAddress(null, address, 0), port)
        } catch (e: UnknownHostException) {
            throw DecodingException("Could not read IPv6 socket address.")
        }
    }
}

@OptIn(ExperimentalUnsignedTypes::class, ExperimentalIoApi::class)
fun BytePacketBuilder.writeNetworkAddress(address: InetSocketAddress) {
    when (val inet = address.address) {
        is Inet4Address -> {
            writeByte(4.toByte())
            val bytes = inet.address
            val complement: Int = (((bytes[0].toInt() and 255) shl 24) or
                ((bytes[1].toInt() and 255) shl 16) or
                ((bytes[2].toInt() and 255) shl 8) or
                (bytes[3].toInt() and 255)).inv()
            writeUInt(complement.toUInt())
            writeUShort(address.port.toUShort())
        }
        is Inet6Address -> {
            writeByte(6.toByte())
            writeUShort(AF_INET6)
            writeUShort(address.port.toUShort())
            writeUInt(0.toUInt())
            writeFully(inet.address)
            writeUInt(0.toUInt())
        }
    }
}
