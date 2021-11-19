package me.hexalite.liteware.protocol.codec

import io.ktor.utils.io.core.*
import me.hexalite.liteware.protocol.packet.MinecraftPacket

abstract class MinecraftPacketCodec<T: MinecraftPacket> {
    abstract suspend fun ByteReadPacket.decode(): T

    abstract suspend fun BytePacketBuilder.encode(packet: T)
}

suspend inline fun <T : MinecraftPacket> MinecraftPacketCodec<T>.encode(output: BytePacketBuilder, packet: T) =
    output.encode(packet)

suspend inline fun <T : MinecraftPacket> MinecraftPacketCodec<T>.decode(input: ByteReadPacket) = input.decode()
