package me.hexalite.liteware.protocol.binary

import io.ktor.utils.io.core.*
import me.hexalite.liteware.protocol.packet.MinecraftPacket

sealed class MinecraftPacketCodec {

    abstract class Encoder: MinecraftPacketCodec() {
        abstract fun Output.encode(packet: MinecraftPacket)
    }

    abstract class Decoder: MinecraftPacketCodec() {
        abstract fun Input.decode(): MinecraftPacket
    }

}
