package me.hexalite.liteware.protocol.packet.inbound

import io.ktor.utils.io.core.*
import me.hexalite.liteware.protocol.annotations.MinecraftPacketInfo
import me.hexalite.liteware.protocol.codec.MinecraftPacketCodec
import me.hexalite.liteware.protocol.packet.InboundPacket

@MinecraftPacketInfo(0x04)
object ClientToServerHandshakePacket: InboundPacket() {

    object Codec: MinecraftPacketCodec<ClientToServerHandshakePacket>() {
        override fun ByteReadPacket.decode(): ClientToServerHandshakePacket {
            return ClientToServerHandshakePacket
        }

        override fun BytePacketBuilder.encode(packet: ClientToServerHandshakePacket) {
            // no-op
        }
    }

}