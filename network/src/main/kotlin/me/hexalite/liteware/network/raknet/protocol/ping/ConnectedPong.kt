package me.hexalite.liteware.network.raknet.protocol.ping

import io.ktor.utils.io.core.*
import me.hexalite.liteware.network.annotations.RakNetPacketInfo
import me.hexalite.liteware.network.codec.RakNetPacketCodec
import me.hexalite.liteware.network.raknet.protocol.RakNetPacket
import me.hexalite.liteware.network.raknet.protocol.RakNetPacketDetails

@RakNetPacketInfo(0x03)
data class ConnectedPong(
    val pingTime: Long,
    val pongTime: Long,
    override val details: RakNetPacketDetails
) : RakNetPacket {

    companion object Codec : RakNetPacketCodec<ConnectedPong>() {

        override fun BytePacketBuilder.encode(packet: ConnectedPong, details: RakNetPacketDetails) {
            writeLong(packet.pingTime)
            writeLong(packet.pongTime)
        }

    }

}
