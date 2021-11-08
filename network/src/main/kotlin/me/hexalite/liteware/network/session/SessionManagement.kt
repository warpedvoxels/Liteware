@file:JvmName("SessionManagement")
package me.hexalite.liteware.network.session

import kotlinx.coroutines.flow.firstOrNull
import me.hexalite.liteware.network.raknet.LitewareRakNetServer
import java.net.SocketAddress

suspend fun LitewareRakNetServer.getCurrentSessionOrNull(address: SocketAddress): NetworkPlayerSession? {
    return sessions.firstOrNull { it.address == address }
}