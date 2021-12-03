package me.hexalite.liteware.network.raknet.protocol.ping

import io.ktor.utils.io.core.*
import me.hexalite.liteware.network.annotations.RakNetPacketIdentity
import me.hexalite.liteware.network.codec.RakNetPacketCodec
import me.hexalite.liteware.network.datatypes.Magic
import me.hexalite.liteware.network.datatypes.readMagic
import me.hexalite.liteware.network.raknet.protocol.RakNetPacket
import me.hexalite.liteware.network.raknet.protocol.RakNetPacketDetails

@RakNetPacketIdentity(id = 0x01)
data class UnconnectedPing(
    val time: Long,
    val magic: Magic,
    val clientGuid: Long,
    override val details: RakNetPacketDetails
) : RakNetPacket {

    companion object Codec : RakNetPacketCodec<UnconnectedPing>() {

        override suspend fun ByteReadPacket.decode(details: RakNetPacketDetails) = UnconnectedPing(
            time = readLong(),
            magic = readMagic() ?: error("Invalid packet; magic not found."),
            clientGuid = readLong(),
            details
        )

    }

}
