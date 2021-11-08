package me.hexalite.liteware.protocol.packet.inbound

import io.ktor.utils.io.core.*
import me.hexalite.liteware.protocol.annotations.MinecraftPacketInfo
import me.hexalite.liteware.protocol.codec.MinecraftPacketCodec
import me.hexalite.liteware.protocol.packet.InboundPacket

@MinecraftPacketInfo(0x04)
object ClientToServerHandshake: InboundPacket() {

    object Codec: MinecraftPacketCodec<ClientToServerHandshake>() {
        override fun ByteReadPacket.decode(): ClientToServerHandshake {
            return ClientToServerHandshake
        }

        override fun BytePacketBuilder.encode(packet: ClientToServerHandshake) {
            // no-op
        }
    }

}