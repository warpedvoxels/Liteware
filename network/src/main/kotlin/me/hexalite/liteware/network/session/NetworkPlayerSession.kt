package me.hexalite.liteware.network.session

import me.hexalite.liteware.network.LitewareRakNetServer
import me.hexalite.liteware.network.raknet.constants.RakNet.MAXIMUM_ENCAPSULATED_HEADER_SIZE
import me.hexalite.liteware.network.raknet.constants.RakNet.RAKNET_DATAGRAM_HEADER_SIZE
import me.hexalite.liteware.network.raknet.constants.RakNet.RAKNET_MTU_PADDING
import java.net.InetSocketAddress

data class NetworkPlayerSession(
    val rakNetProtocolVersion: UByte,
    val address: InetSocketAddress,
    val mtu: Short,
    val server: LitewareRakNetServer,
    val guid: Long = 0
)

inline val NetworkPlayerSession.adjustedMtu: Short
    get() = (mtu - RAKNET_MTU_PADDING - MAXIMUM_ENCAPSULATED_HEADER_SIZE - RAKNET_DATAGRAM_HEADER_SIZE).toShort()

