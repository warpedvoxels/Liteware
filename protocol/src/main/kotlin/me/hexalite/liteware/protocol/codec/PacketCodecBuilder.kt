package me.hexalite.liteware.protocol.codec

import io.ktor.utils.io.core.*
import me.hexalite.liteware.protocol.packet.EmptyMinecraftPacket
import me.hexalite.liteware.protocol.packet.MinecraftPacket

typealias EncoderInstruction = BytePacketBuilder.(packet: MinecraftPacket) -> Unit

typealias DecoderInstruction = ByteReadPacket.() -> MinecraftPacket

data class PacketCodecBuilder(
    var encoderInstruction: EncoderInstruction = { },
    var decoderInstruction: DecoderInstruction = { EmptyMinecraftPacket }
) {

    fun encoder(instruction: EncoderInstruction) {
        encoderInstruction = instruction
    }

    fun decoder(instruction: DecoderInstruction) {
        decoderInstruction = instruction
    }

    fun build() = CallbackBasedPacketCodec(encoderInstruction, decoderInstruction)

}

class CallbackBasedPacketCodec(
    private val encoder: EncoderInstruction, private val decoder: DecoderInstruction
) : MinecraftPacketCodec.Decoder, MinecraftPacketCodec.Encoder {

    override fun ByteReadPacket.decode(): MinecraftPacket = decoder(this)

    override fun BytePacketBuilder.encode(packet: MinecraftPacket) = encoder(packet)

}

inline fun MinecraftPacket.codec(builder: PacketCodecBuilder.() -> Unit)
    = PacketCodecBuilder().apply(builder).build()
