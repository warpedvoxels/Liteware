package me.hexalite.liteware.network.raknet.protocol.ping

import io.ktor.utils.io.core.*
import me.hexalite.liteware.network.annotations.RakNetPacketIdentity
import me.hexalite.liteware.network.codec.RakNetPacketCodec
import me.hexalite.liteware.network.raknet.protocol.RakNetPacket
import me.hexalite.liteware.network.raknet.protocol.RakNetPacketDetails

@RakNetPacketIdentity(0x00)
data class ConnectedPing(val time: Long, override val details: RakNetPacketDetails): RakNetPacket {

    companion object Codec: RakNetPacketCodec<ConnectedPing>() {

        override suspend fun ByteReadPacket.decode(details: RakNetPacketDetails) = ConnectedPing(readLong(), details)

    }

}
