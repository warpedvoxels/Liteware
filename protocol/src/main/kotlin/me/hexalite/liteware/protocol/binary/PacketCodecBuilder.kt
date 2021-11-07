package me.hexalite.liteware.protocol.binary

import io.ktor.utils.io.core.*
import me.hexalite.liteware.protocol.packet.EmptyMinecraftPacket
import me.hexalite.liteware.protocol.packet.MinecraftPacket

typealias EncoderInstruction = Output.(packet: MinecraftPacket) -> Unit

typealias DecoderInstruction = Input.() -> MinecraftPacket

data class PacketCodecBuilder(
    var encoderInstruction: EncoderInstruction = { },
    var decoderInstruction: DecoderInstruction = { EmptyMinecraftPacket }
) {

    fun encode(instruction: EncoderInstruction) {
        encoderInstruction = instruction
    }

    fun decode(instruction: DecoderInstruction) {
        decoderInstruction = instruction
    }

    fun build() = CallbackBasedPacketCodec(encoderInstruction, decoderInstruction)

}

class CallbackBasedPacketCodec(
    private val encoder: EncoderInstruction, private val decoder: DecoderInstruction
) : MinecraftPacketCodec.Decoder, MinecraftPacketCodec.Encoder {

    override fun Input.decode(): MinecraftPacket = decoder(this)

    override fun Output.encode(packet: MinecraftPacket) = encoder(packet)

}

inline fun MinecraftPacket.codec(builder: PacketCodecBuilder.() -> Unit)
    = PacketCodecBuilder().apply(builder).build()
