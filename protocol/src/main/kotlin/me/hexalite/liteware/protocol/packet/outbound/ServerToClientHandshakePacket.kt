package me.hexalite.liteware.protocol.packet.outbound

import io.ktor.utils.io.core.*
import me.hexalite.liteware.protocol.annotations.MinecraftPacketInfo
import me.hexalite.liteware.protocol.codec.MinecraftPacketCodec
import me.hexalite.liteware.protocol.datatypes.readMinecraftString
import me.hexalite.liteware.protocol.datatypes.writeMinecraftString
import me.hexalite.liteware.protocol.packet.OutboundPacket

@MinecraftPacketInfo(id = 0x03)
data class ServerToClientHandshakePacket(val jwtData: String) : OutboundPacket() {

    companion object Codec : MinecraftPacketCodec<ServerToClientHandshakePacket>() {
        override suspend fun ByteReadPacket.decode() = ServerToClientHandshakePacket(readMinecraftString())

        override suspend fun BytePacketBuilder.encode(packet: ServerToClientHandshakePacket) =
            writeMinecraftString(packet.jwtData)
    }

}