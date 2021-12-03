package me.hexalite.liteware.network.raknet.protocol.ping

import io.ktor.utils.io.core.*
import me.hexalite.liteware.network.annotations.RakNetPacketIdentity
import me.hexalite.liteware.network.codec.RakNetPacketCodec
import me.hexalite.liteware.network.datatypes.Magic
import me.hexalite.liteware.network.datatypes.writeMagic
import me.hexalite.liteware.network.datatypes.writeRakNetString
import me.hexalite.liteware.network.raknet.protocol.RakNetPacket
import me.hexalite.liteware.network.raknet.protocol.RakNetPacketDetails

@RakNetPacketIdentity(0x1c)
data class UnconnectedPong(
    val timestamp: Long,
    val serverGuid: Long,
    val magic: Magic,
    val serverId: String,
    override val details: RakNetPacketDetails
) : RakNetPacket {

    companion object Codec : RakNetPacketCodec<UnconnectedPong>() {

        override suspend fun BytePacketBuilder.encode(packet: UnconnectedPong, details: RakNetPacketDetails) {
            writeLong(packet.timestamp)
            writeLong(packet.serverGuid)
            writeMagic()
            writeRakNetString(packet.serverId)
        }

    }

}
