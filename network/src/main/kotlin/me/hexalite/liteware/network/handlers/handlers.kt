@file:JvmName("Handlers")

package me.hexalite.liteware.network.handlers

import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import me.hexalite.liteware.network.bootstrap.LitewareNetworkBootstrap
import me.hexalite.liteware.network.raknet.protocol.RakNetPacket

fun interface RakNetHandler<T : RakNetPacket> {
    suspend fun middleware(event: T)
}

inline fun <reified T : RakNetPacket> LitewareNetworkBootstrap.onEachPacket(handler: RakNetHandler<T>) =
    rakNet.rakNetPackets.filterIsInstance<T>().onEach { handler.middleware(it) }.launchIn(handlersScope)

internal suspend fun LitewareNetworkBootstrap.initializeHandlers() {
    handleCustomPackets()
    handleConnections()
}