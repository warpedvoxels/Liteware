package me.hexalite.liteware.protocol.packet.inbound

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import me.hexalite.liteware.protocol.codec.codec
import me.hexalite.liteware.protocol.datatypes.readIntBigEndian
import me.hexalite.liteware.protocol.datatypes.readMinecraftString
import me.hexalite.liteware.protocol.datatypes.writeIntBigEndian
import me.hexalite.liteware.protocol.datatypes.writeMinecraftString
import me.hexalite.liteware.protocol.packet.InboundPacket

data class LoginPacket(
    val protocolVersion: Int,
    val chainData: List<String>,
    val skinData: String
) : InboundPacket() {

    override val id: Int
        get() = 0x01

    override val codec = codec {
        encoder {
            writeIntBigEndian(protocolVersion)
            writeMinecraftString(Json.encodeToString(chainData))
            writeMinecraftString(skinData)
        }
        decoder {
            LoginPacket(
                readIntBigEndian(),
                Json.decodeFromString(readMinecraftString()),
                readMinecraftString()
            )
        }
    }

    companion object {
        val Empty = LoginPacket(0, listOf(), "")
    }

}
