package me.hexalite.liteware.network.raknet.protocol.login

import io.ktor.utils.io.core.*
import me.hexalite.liteware.network.annotations.RakNetPacketInfo
import me.hexalite.liteware.network.codec.RakNetPacketCodec
import me.hexalite.liteware.network.datatypes.writeNetworkAddress
import me.hexalite.liteware.network.raknet.protocol.RakNetPacket
import me.hexalite.liteware.network.raknet.protocol.RakNetPacketDetails
import java.net.Inet4Address
import java.net.InetSocketAddress

@RakNetPacketInfo(0x10)
@OptIn(ExperimentalStdlibApi::class)
data class ConnectionRequestAccepted(
    val clientAddress: InetSocketAddress,
    val time: Long,
    val requestTime: Long,
    override val details: RakNetPacketDetails
) : RakNetPacket {

    companion object Codec : RakNetPacketCodec<ConnectionRequestAccepted>() {
        private val ipV4NetworkAddresses = buildList {
            add(InetSocketAddress("127.0.0.1", 0))
            repeat(9) {
                add(InetSocketAddress("0.0.0.0", 0))
            }
        }

        private val ipV6NetworkAddresses = buildList {
            add(InetSocketAddress("::1", 0))
            repeat(9) {
                add(InetSocketAddress("::0", 0))
            }
        }

        override suspend fun BytePacketBuilder.encode(packet: ConnectionRequestAccepted, details: RakNetPacketDetails) {
            writeNetworkAddress(packet.clientAddress)
            writeShort(0)
            (if (packet.clientAddress.address is Inet4Address) ipV4NetworkAddresses else ipV6NetworkAddresses)
                .forEach(::writeNetworkAddress)
            writeLong(packet.time)
            writeLong(packet.requestTime)
        }

    }

}
