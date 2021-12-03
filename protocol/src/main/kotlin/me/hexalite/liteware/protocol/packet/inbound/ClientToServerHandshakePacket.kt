package me.hexalite.liteware.protocol.packet.inbound

import io.ktor.utils.io.core.*
import me.hexalite.liteware.protocol.annotations.MinecraftPacketIdentity
import me.hexalite.liteware.protocol.codec.MinecraftPacketCodec
import me.hexalite.liteware.protocol.packet.InboundPacket

@MinecraftPacketIdentity(0x04)
object ClientToServerHandshakePacket: InboundPacket() {

    object Codec: MinecraftPacketCodec<ClientToServerHandshakePacket>() {
        override suspend fun ByteReadPacket.decode(): ClientToServerHandshakePacket {
            return ClientToServerHandshakePacket
        }

        override suspend fun BytePacketBuilder.encode(packet: ClientToServerHandshakePacket) {
            // no-op
        }
    }

}