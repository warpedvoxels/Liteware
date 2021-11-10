package me.hexalite.liteware.network.raknet.protocol.connection

import io.ktor.utils.io.core.*
import me.hexalite.liteware.network.annotations.RakNetPacketInfo
import me.hexalite.liteware.network.codec.RakNetPacketCodec
import me.hexalite.liteware.network.datatypes.Magic
import me.hexalite.liteware.network.datatypes.writeMagic
import me.hexalite.liteware.network.raknet.constants.RakNet.USE_SECURITY
import me.hexalite.liteware.network.raknet.protocol.RakNetPacket
import me.hexalite.liteware.network.raknet.protocol.RakNetPacketDetails
import me.hexalite.liteware.protocol.datatypes.writeBoolean

@RakNetPacketInfo(0x06)
data class OpenConnectionReplyOne(
    val magic: Magic,
    val serverGuid: Long,
    val mtu: Short,
    override val details: RakNetPacketDetails
) : RakNetPacket {

    companion object Codec : RakNetPacketCodec<OpenConnectionReplyOne>() {

        override fun BytePacketBuilder.encode(packet: OpenConnectionReplyOne, details: RakNetPacketDetails) {
            writeMagic()
            writeLong(packet.serverGuid)
            writeBoolean(USE_SECURITY)
            writeShort(packet.mtu)
        }

    }

}
