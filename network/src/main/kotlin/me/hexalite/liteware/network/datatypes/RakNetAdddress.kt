@file:JvmName("RakNetAddress")
package me.hexalite.liteware.network.datatypes

import io.ktor.util.network.*
import io.ktor.utils.io.core.*
import me.hexalite.liteware.network.handlers.isIPv6
import kotlin.experimental.and
import kotlin.experimental.inv

private fun flip(bytes: ByteArray) {
    for (i in bytes.indices) {
        bytes[i] = bytes[i].inv() and 0xFF.toByte()
    }
}

private fun getIPv6ScopeId(host: String): Int {
    val index = host.indexOf('%')
    return if (index == -1) 0 else host.substring(index + 1).toInt()
}

@OptIn(ExperimentalUnsignedTypes::class)
fun ByteReadPacket.readNetworkAddress(): NetworkAddress {
    val version = readByte()
    val port = readUShort()
    val host = when (version) {
        4.toByte() -> {
            val host = readBytes(4)
            flip(host)
            host.toString(Charsets.UTF_8)
        }
        6.toByte() -> {
            val host = readBytes(16)
            val scopeId = readInt()
            val hostString = host.toString(Charsets.UTF_8)
            "$hostString%$scopeId"
        }
        else -> throw IllegalArgumentException("Invalid address version: $version")
    }
    return NetworkAddress(host, port.toInt())
}

@OptIn(ExperimentalUnsignedTypes::class, ExperimentalIoApi::class)
fun BytePacketBuilder.writeNetworkAddress(address: NetworkAddress) {
    val host = address.hostname
    val hostBytes = when {
        isIPv6(host) -> {
            val scopeId = getIPv6ScopeId(host)
            val hostBytes = host.split('%')[0].toByteArray(Charsets.UTF_8)
            writeByte(6)
            writeFully(hostBytes)
            writeInt(scopeId)
            hostBytes
        }
        else -> {
            val hostBytes = host.toByteArray(Charsets.UTF_8)
            writeByte(4)
            writeFully(hostBytes)
            hostBytes
        }
    }
    writeUShort(address.port.toUShort())
    writeFully(hostBytes)
}
