@file:JvmName("RakNetString")
package me.hexalite.liteware.network.datatypes

import io.ktor.utils.io.core.*

@OptIn(ExperimentalUnsignedTypes::class)
fun BytePacketBuilder.writeRakNetString(string: String) {
    writeUShort(string.length.toUShort())
    writeFully(string.encodeToByteArray())
}
