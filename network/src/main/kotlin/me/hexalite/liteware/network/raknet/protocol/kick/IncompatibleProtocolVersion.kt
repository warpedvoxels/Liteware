package me.hexalite.liteware.network.raknet.protocol.kick

import io.ktor.utils.io.core.*
import me.hexalite.liteware.network.annotations.RakNetPacketInfo
import me.hexalite.liteware.network.codec.RakNetPacketCodec
import me.hexalite.liteware.network.datatypes.Magic
import me.hexalite.liteware.network.datatypes.writeMagic
import me.hexalite.liteware.network.raknet.protocol.RakNetPacket
import me.hexalite.liteware.network.raknet.protocol.RakNetPacketDetails

@RakNetPacketInfo(0x19)
data class IncompatibleProtocolVersion(
    val protocol: Byte,
    val magic: Magic,
    val serverGuid: Long,
    override val details: RakNetPacketDetails
) : RakNetPacket {

    companion object Codec : RakNetPacketCodec<IncompatibleProtocolVersion>() {

        @OptIn(ExperimentalIoApi::class)
        override suspend fun BytePacketBuilder.encode(packet: IncompatibleProtocolVersion, details: RakNetPacketDetails) {
            writeByte(packet.protocol)
            writeMagic()
            writeLong(packet.serverGuid)
        }

    }

}
