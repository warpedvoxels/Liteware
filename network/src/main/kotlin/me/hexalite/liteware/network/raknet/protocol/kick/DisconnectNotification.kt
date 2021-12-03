package me.hexalite.liteware.network.raknet.protocol.kick

import io.ktor.utils.io.core.*
import me.hexalite.liteware.network.annotations.RakNetPacketIdentity
import me.hexalite.liteware.network.codec.RakNetPacketCodec
import me.hexalite.liteware.network.raknet.protocol.RakNetPacket
import me.hexalite.liteware.network.raknet.protocol.RakNetPacketDetails

@RakNetPacketIdentity(id = 0x15)
data class DisconnectNotification(
    override val details: RakNetPacketDetails
) : RakNetPacket {

    companion object Codec : RakNetPacketCodec<DisconnectNotification>() {

        override suspend fun ByteReadPacket.decode(details: RakNetPacketDetails) = DisconnectNotification(details)

        override suspend fun BytePacketBuilder.encode(packet: DisconnectNotification, details: RakNetPacketDetails) {
            // no-op
        }

    }

}
