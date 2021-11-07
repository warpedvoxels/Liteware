package me.hexalite.liteware.protocol.codec

import io.ktor.utils.io.core.*
import me.hexalite.liteware.protocol.packet.MinecraftPacket

sealed interface MinecraftPacketCodec {

    interface Encoder : MinecraftPacketCodec {
        fun Output.encode(packet: MinecraftPacket)
    }

    interface Decoder : MinecraftPacketCodec {
        fun Input.decode(): MinecraftPacket
    }

}

inline fun MinecraftPacketCodec.encode(output: Output, packet: MinecraftPacket) =
    with(this as MinecraftPacketCodec.Encoder) {
        output.encode(packet)
    }

inline fun MinecraftPacketCodec.decode(input: Input) =
    with(this as MinecraftPacketCodec.Decoder) {
        input.decode()
    }