@file:JvmName("Handlers")

package me.hexalite.liteware.network.handlers

import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import me.hexalite.liteware.network.bootstrap.LitewareNetworkBootstrap
import me.hexalite.liteware.network.raknet.protocol.RakNetPacket
import me.hexalite.liteware.network.raknet.protocol.custom.NetworkMinecraftPacket

fun interface NetworkPacketHandler<T> {
    suspend fun middleware(event: T)
}

inline fun <reified T : RakNetPacket> LitewareNetworkBootstrap.onEachPacket(handler: NetworkPacketHandler<T>) =
    rakNet.rakNetPackets.filterIsInstance<T>().onEach { handler.middleware(it) }.launchIn(handlersScope)

inline fun LitewareNetworkBootstrap.onEachGameNetworkPacket(handler: NetworkPacketHandler<NetworkMinecraftPacket>) =
    rakNet.packets.onEach { handler.middleware(it) }.launchIn(handlersScope)

internal suspend fun LitewareNetworkBootstrap.initializeHandlers() {
    handleCustomPackets()
    handleConnections()
}