@file:JvmName("PingHandler")
package me.hexalite.liteware.network.handlers

import io.ktor.utils.io.core.*
import me.hexalite.liteware.network.bootstrap.LitewareNetworkBootstrap
import me.hexalite.liteware.network.packets.UnconnectedPing
import me.hexalite.liteware.network.unconnectedMagicOrNull

// todo
internal fun LitewareNetworkBootstrap.handlePings() = onEachPacket<UnconnectedPing> { (datagram) ->
    println("Ping received.")
    val packet = datagram.packet

    // RakNet Bedrock Edition - Unconnected Ping packet
    val pingTime = packet.readLong()
    val pingId = packet.readLong()
    unconnectedMagicOrNull(packet) ?: return@onEachPacket
    val guid = packet.readLong()
}
