@file:JvmName("SessionManagement")
package me.hexalite.liteware.network.session

import kotlinx.coroutines.flow.firstOrNull
import me.hexalite.liteware.network.LitewareRakNetServer
import java.net.SocketAddress

suspend fun LitewareRakNetServer.findCurrentSessionOrNull(address: SocketAddress): NetworkPlayerSession? =
    sessions.firstOrNull { it.address == address }
