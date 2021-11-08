package me.hexalite.liteware.protocol.packet.inbound

import io.ktor.utils.io.core.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.hexalite.liteware.protocol.annotations.MinecraftPacketInfo
import me.hexalite.liteware.protocol.codec.MinecraftPacketCodec
import me.hexalite.liteware.protocol.datatypes.readIntBigEndian
import me.hexalite.liteware.protocol.datatypes.readMinecraftString
import me.hexalite.liteware.protocol.datatypes.writeIntBigEndian
import me.hexalite.liteware.protocol.datatypes.writeMinecraftString
import me.hexalite.liteware.protocol.packet.InboundPacket

@MinecraftPacketInfo(id = 0x01)
data class LoginPacket(val protocolVersion: Int, val chainData: List<String>, val skinData: String) : InboundPacket() {

    companion object Codec: MinecraftPacketCodec<LoginPacket>() {

        override fun BytePacketBuilder.encode(packet: LoginPacket) = with(packet) {
            writeIntBigEndian(protocolVersion)
            writeMinecraftString(Json.encodeToString(chainData))
            writeMinecraftString(skinData)
        }

        override fun ByteReadPacket.decode() = LoginPacket(
            readIntBigEndian(),
            Json.decodeFromString(readMinecraftString()),
            readMinecraftString()
        )

    }

}
