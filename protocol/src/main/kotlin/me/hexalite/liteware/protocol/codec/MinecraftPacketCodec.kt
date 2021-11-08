package me.hexalite.liteware.protocol.codec

import io.ktor.utils.io.core.*
import me.hexalite.liteware.protocol.packet.MinecraftPacket

sealed interface MinecraftPacketCodec {

    interface Encoder : MinecraftPacketCodec {
        fun BytePacketBuilder.encode(packet: MinecraftPacket)
    }

    interface Decoder : MinecraftPacketCodec {
        fun ByteReadPacket.decode(): MinecraftPacket
    }

}

inline fun MinecraftPacketCodec.encoder(output: BytePacketBuilder, packet: MinecraftPacket) =
    with(this as MinecraftPacketCodec.Encoder) {
        output.encode(packet)
    }

inline fun MinecraftPacketCodec.decoder(input: ByteReadPacket) =
    with(this as MinecraftPacketCodec.Decoder) {
        input.decode()
    }