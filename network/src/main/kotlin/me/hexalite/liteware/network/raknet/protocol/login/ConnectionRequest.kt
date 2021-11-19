package me.hexalite.liteware.network.raknet.protocol.login

import io.ktor.utils.io.core.*
import me.hexalite.liteware.network.annotations.RakNetPacketInfo
import me.hexalite.liteware.network.codec.RakNetPacketCodec
import me.hexalite.liteware.network.raknet.protocol.RakNetPacket
import me.hexalite.liteware.network.raknet.protocol.RakNetPacketDetails
import me.hexalite.liteware.protocol.datatypes.readBoolean

@RakNetPacketInfo(0x09)
data class ConnectionRequest(
    val clientGuid: Long,
    val time: Long,
    val useSecurity: Boolean,
    override val details: RakNetPacketDetails
) : RakNetPacket {

    companion object Codec : RakNetPacketCodec<ConnectionRequest>() {

        override suspend fun ByteReadPacket.decode(details: RakNetPacketDetails) =
            ConnectionRequest(readLong(), readLong(), readBoolean(), details)

    }

}
