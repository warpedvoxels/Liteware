@file:JvmName("Handlers")

package me.hexalite.liteware.network.handlers

import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import me.hexalite.liteware.network.bootstrap.LitewareNetworkBootstrap
import me.hexalite.liteware.network.packets.RakNetPacket
import me.hexalite.liteware.network.utils.Executor

fun interface RakNetHandler<T : RakNetPacket> : Executor<T>

inline fun <reified T : RakNetPacket> LitewareNetworkBootstrap.onEachPacket(handler: RakNetHandler<T>) =
    rakNet.rakNetPackets.filterIsInstance<T>().onEach { handler.middleware(it) }.launchIn(handlersScope)

internal suspend fun LitewareNetworkBootstrap.initializeHandlers() {
    handleMinecraftPackets()
    handleConnections()
    handlePings()
}