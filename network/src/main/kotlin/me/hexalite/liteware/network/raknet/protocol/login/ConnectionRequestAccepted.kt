package me.hexalite.liteware.network.raknet.protocol.login

import io.ktor.util.network.*
import io.ktor.utils.io.core.*
import me.hexalite.liteware.network.annotations.RakNetPacketInfo
import me.hexalite.liteware.network.codec.RakNetPacketCodec
import me.hexalite.liteware.network.datatypes.writeNetworkAddress
import me.hexalite.liteware.network.raknet.protocol.RakNetPacket
import me.hexalite.liteware.network.raknet.protocol.RakNetPacketDetails

@RakNetPacketInfo(0x10)
@OptIn(ExperimentalStdlibApi::class)
data class ConnectionRequestAccepted(
    val clientAddress: NetworkAddress,
    val systemIndex: Short = 0,
    val internalIds: List<NetworkAddress> = buildList {
        repeat(10) {
            add(NetworkAddress("255.255.255.255", 19132))
        }
    },
    val requestTime: Long,
    val time: Long,
    override val details: RakNetPacketDetails
): RakNetPacket {

    companion object Codec: RakNetPacketCodec<ConnectionRequestAccepted>() {

        override fun BytePacketBuilder.encode(packet: ConnectionRequestAccepted, details: RakNetPacketDetails) {
            writeNetworkAddress(packet.clientAddress)
            writeShort(packet.systemIndex)
            packet.internalIds.forEach(this::writeNetworkAddress)
            writeLong(packet.requestTime)
            writeLong(packet.time)
        }

    }

}