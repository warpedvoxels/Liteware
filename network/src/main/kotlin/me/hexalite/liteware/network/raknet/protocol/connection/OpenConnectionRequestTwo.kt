package me.hexalite.liteware.network.raknet.protocol.connection

import io.ktor.util.network.*
import io.ktor.utils.io.core.*
import me.hexalite.liteware.network.annotations.RakNetPacketInfo
import me.hexalite.liteware.network.codec.RakNetPacketCodec
import me.hexalite.liteware.network.datatypes.Magic
import me.hexalite.liteware.network.datatypes.readMagic
import me.hexalite.liteware.network.datatypes.readNetworkAddress
import me.hexalite.liteware.network.raknet.protocol.RakNetPacket
import me.hexalite.liteware.network.raknet.protocol.RakNetPacketDetails

@RakNetPacketInfo(0x07)
data class OpenConnectionRequestTwo(
    val magic: Magic,
    val serverAddress: NetworkAddress,
    val mtu: Short,
    val clientGuid: Long,
    override val details: RakNetPacketDetails
) : RakNetPacket {

    companion object Codec : RakNetPacketCodec<OpenConnectionRequestTwo>() {

        override fun ByteReadPacket.decode(details: RakNetPacketDetails) = OpenConnectionRequestTwo(
            magic = readMagic() ?: error("Invalid open connection request two; magic not found."),
            serverAddress = readNetworkAddress(),
            mtu = readShort(),
            clientGuid = readLong(),
            details
        )

    }

}
