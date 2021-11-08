package me.hexalite.liteware.protocol.codec

import io.ktor.utils.io.core.*
import me.hexalite.liteware.protocol.packet.MinecraftPacket

abstract class MinecraftPacketCodec<T: MinecraftPacket> {
    abstract fun ByteReadPacket.decode(): T

    abstract fun BytePacketBuilder.encode(packet: T)
}

inline fun <T: MinecraftPacket> MinecraftPacketCodec<T>.encode(output: BytePacketBuilder, packet: T) =
    output.encode(packet)

inline fun <T: MinecraftPacket> MinecraftPacketCodec<T>.decode(input: ByteReadPacket) = input.decode()
