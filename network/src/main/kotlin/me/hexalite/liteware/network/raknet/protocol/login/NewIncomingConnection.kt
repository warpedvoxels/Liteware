package me.hexalite.liteware.network.raknet.protocol.login

import io.ktor.util.network.*
import io.ktor.utils.io.core.*
import me.hexalite.liteware.network.annotations.RakNetPacketInfo
import me.hexalite.liteware.network.codec.RakNetPacketCodec
import me.hexalite.liteware.network.datatypes.readNetworkAddress
import me.hexalite.liteware.network.raknet.protocol.RakNetPacket
import me.hexalite.liteware.network.raknet.protocol.RakNetPacketDetails

@RakNetPacketInfo(0x13)
data class NewIncomingConnection(
    val serverAddress: NetworkAddress,
    val internalAddress: NetworkAddress,
    override val details: RakNetPacketDetails
) : RakNetPacket {

    companion object Codec : RakNetPacketCodec<NewIncomingConnection>() {

        override fun ByteReadPacket.decode(details: RakNetPacketDetails): NewIncomingConnection {
            val serverAddress = readNetworkAddress()
            val internalAddress = readNetworkAddress()
            return NewIncomingConnection(serverAddress, internalAddress, details)
        }

    }

}
