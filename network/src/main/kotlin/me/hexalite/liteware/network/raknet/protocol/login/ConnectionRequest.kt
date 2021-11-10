package me.hexalite.liteware.network.raknet.protocol.login

import io.ktor.utils.io.core.*
import me.hexalite.liteware.network.annotations.RakNetPacketInfo
import me.hexalite.liteware.network.codec.RakNetPacketCodec
import me.hexalite.liteware.network.raknet.protocol.RakNetPacket
import me.hexalite.liteware.network.raknet.protocol.RakNetPacketDetails

@RakNetPacketInfo(0x09)
data class ConnectionRequest(
    val clientGuid: Long,
    val time: Long,
    override val details: RakNetPacketDetails
) : RakNetPacket {

    companion object Codec : RakNetPacketCodec<ConnectionRequest>() {

        override fun ByteReadPacket.decode(details: RakNetPacketDetails) =
            ConnectionRequest(readLong(), readLong(), details)

    }

}
