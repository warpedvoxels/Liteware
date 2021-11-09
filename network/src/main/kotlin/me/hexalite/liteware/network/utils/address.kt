@file:JvmName("NetworkAddressUtils")
package me.hexalite.liteware.network.utils

import io.ktor.network.sockets.*
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

@OptIn(ExperimentalIoApi::class)
fun BytePacketBuilder.writeAddress(datagram: Datagram) {
    val address = datagram.address
    val host = address.hostname.encodeToByteArray()
    if(isIPv6(address.hostname)) {
        writeByte(6)
        writeShortLittleEndian(23)
        writeShort(address.port.toShort())
        writeInt(0)
        writeFully(address.hostname.toByteArray())
        writeInt(getIPv6ScopeId(address.hostname))
    } else {
        writeByte(4)
        flip(host)
        writeFully(host)
        writeShort(address.port.toShort())
    }
}
