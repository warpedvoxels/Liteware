package me.hexalite.liteware.network.raknet.protocol.connection

import io.ktor.utils.io.core.*
import me.hexalite.liteware.network.annotations.RakNetPacketInfo
import me.hexalite.liteware.network.codec.RakNetPacketCodec
import me.hexalite.liteware.network.datatypes.Magic
import me.hexalite.liteware.network.datatypes.readMagic
import me.hexalite.liteware.network.raknet.constants.RakNet.RAKNET_MTU_PADDING
import me.hexalite.liteware.network.raknet.protocol.RakNetPacket
import me.hexalite.liteware.network.raknet.protocol.RakNetPacketDetails

@RakNetPacketInfo(0x05)
@OptIn(ExperimentalUnsignedTypes::class)
data class OpenConnectionRequestOne(
    val magic: Magic,
    val rakNetProtocolVersion: UByte,
    val mtu: Short, override val details: RakNetPacketDetails
) : RakNetPacket {

    companion object Codec : RakNetPacketCodec<OpenConnectionRequestOne>() {

        override fun ByteReadPacket.decode(details: RakNetPacketDetails) = OpenConnectionRequestOne(
            readMagic() ?: error("Invalid open connection request one; magic not found."),
            readUByte(),
            (remaining + RAKNET_MTU_PADDING).toShort(),
            details
        )

    }

}