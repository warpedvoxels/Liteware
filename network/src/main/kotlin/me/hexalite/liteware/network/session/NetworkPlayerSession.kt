package me.hexalite.liteware.network.session

import me.hexalite.liteware.network.raknet.LitewareRakNetServer
import me.hexalite.liteware.protocol.ProtocolVersion
import java.net.SocketAddress

data class NetworkPlayerSession(
    val protocolVersion: ProtocolVersion,
    val address: SocketAddress,
    val mtu: Int,
    val server: LitewareRakNetServer
)
