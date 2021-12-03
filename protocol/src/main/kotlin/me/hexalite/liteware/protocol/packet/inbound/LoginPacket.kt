package me.hexalite.liteware.protocol.packet.inbound

import io.ktor.utils.io.core.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.hexalite.liteware.protocol.annotations.MinecraftPacketIdentity
import me.hexalite.liteware.protocol.codec.MinecraftPacketCodec
import me.hexalite.liteware.protocol.datatypes.readIntBigEndian
import me.hexalite.liteware.protocol.datatypes.readMinecraftString
import me.hexalite.liteware.protocol.datatypes.writeIntBigEndian
import me.hexalite.liteware.protocol.datatypes.writeMinecraftString
import me.hexalite.liteware.protocol.packet.InboundPacket

@MinecraftPacketIdentity(id = 0x01)
data class LoginPacket(val protocolVersion: Int, val chainData: List<String>, val skinData: String) : InboundPacket() {

    companion object Codec: MinecraftPacketCodec<LoginPacket>() {

        override suspend fun BytePacketBuilder.encode(packet: LoginPacket) = with(packet) {
            writeIntBigEndian(protocolVersion)
            writeMinecraftString(Json.encodeToString(chainData))
            writeMinecraftString(skinData)
        }

        override suspend fun ByteReadPacket.decode() = LoginPacket(
            readIntBigEndian(),
            Json.decodeFromString(readMinecraftString()),
            readMinecraftString()
        )

    }

}
