package me.hexalite.liteware.protocol.packet.outbound

import io.ktor.utils.io.core.*
import me.hexalite.liteware.protocol.annotations.MinecraftPacketInfo
import me.hexalite.liteware.protocol.auth.LoginStatus
import me.hexalite.liteware.protocol.codec.MinecraftPacketCodec
import me.hexalite.liteware.protocol.datatypes.readIntBigEndian
import me.hexalite.liteware.protocol.datatypes.writeIntBigEndian
import me.hexalite.liteware.protocol.packet.OutboundPacket

@MinecraftPacketInfo(id = 0x02)
data class PlayStatusPacket(val loginStatus: LoginStatus): OutboundPacket() {

    companion object Codec: MinecraftPacketCodec<PlayStatusPacket>() {

        override fun ByteReadPacket.decode() = PlayStatusPacket(LoginStatus(readIntBigEndian()))

        override fun BytePacketBuilder.encode(packet: PlayStatusPacket) = writeIntBigEndian(packet.loginStatus.ordinal)

    }

}
