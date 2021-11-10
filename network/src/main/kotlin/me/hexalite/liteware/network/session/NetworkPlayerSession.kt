package me.hexalite.liteware.network.session

import me.hexalite.liteware.network.LitewareRakNetServer
import java.net.SocketAddress

data class NetworkPlayerSession(
    val rakNetProtocolVersion: UByte,
    val address: SocketAddress,
    val mtu: Short,
    val server: LitewareRakNetServer
)
