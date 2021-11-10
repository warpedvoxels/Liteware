package me.hexalite.liteware.network.raknet.protocol.kick

import io.ktor.utils.io.core.*
import me.hexalite.liteware.network.annotations.RakNetPacketInfo
import me.hexalite.liteware.network.codec.RakNetPacketCodec
import me.hexalite.liteware.network.raknet.protocol.RakNetPacket
import me.hexalite.liteware.network.raknet.protocol.RakNetPacketDetails

@RakNetPacketInfo(id = 0x15)
data class DisconnectNotification(
    override val details: RakNetPacketDetails
) : RakNetPacket, RakNetPacketCodec<DisconnectNotification>() {

    override fun ByteReadPacket.decode(details: RakNetPacketDetails) = DisconnectNotification(details)

    override fun BytePacketBuilder.encode(packet: DisconnectNotification, details: RakNetPacketDetails) {
        // no-op
    }

}
