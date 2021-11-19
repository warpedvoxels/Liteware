package me.hexalite.liteware.network.raknet.protocol.connection

import io.ktor.utils.io.core.*
import me.hexalite.liteware.network.annotations.RakNetPacketInfo
import me.hexalite.liteware.network.codec.RakNetPacketCodec
import me.hexalite.liteware.network.datatypes.Magic
import me.hexalite.liteware.network.datatypes.writeMagic
import me.hexalite.liteware.network.datatypes.writeNetworkAddress
import me.hexalite.liteware.network.raknet.constants.RakNet
import me.hexalite.liteware.network.raknet.protocol.RakNetPacket
import me.hexalite.liteware.network.raknet.protocol.RakNetPacketDetails
import me.hexalite.liteware.protocol.datatypes.writeBoolean
import java.net.InetSocketAddress

@RakNetPacketInfo(0x08)
data class OpenConnectionReplyTwo(
    val magic: Magic,
    val serverGuid: Long,
    val clientAddress: InetSocketAddress,
    val mtu: Short,
    override val details: RakNetPacketDetails
) : RakNetPacket {

    companion object Codec : RakNetPacketCodec<OpenConnectionReplyTwo>() {

        override suspend fun BytePacketBuilder.encode(packet: OpenConnectionReplyTwo, details: RakNetPacketDetails) {
            writeMagic()
            writeLong(packet.serverGuid)
            writeNetworkAddress(packet.clientAddress)
            writeShort(packet.mtu)
            writeBoolean(RakNet.USE_SECURITY)
        }

    }

}
