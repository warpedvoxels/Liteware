@file:JvmName("LoginHandler")

package me.hexalite.liteware.network.handlers

import io.ktor.utils.io.core.*
import me.hexalite.liteware.api.LitewareAPI
import me.hexalite.liteware.network.bootstrap.LitewareNetworkBootstrap
import me.hexalite.liteware.network.datatypes.Magic
import me.hexalite.liteware.network.raknet.protocol.connection.OpenConnectionReplyOne
import me.hexalite.liteware.network.raknet.protocol.connection.OpenConnectionReplyTwo
import me.hexalite.liteware.network.raknet.protocol.connection.OpenConnectionRequestOne
import me.hexalite.liteware.network.raknet.protocol.connection.OpenConnectionRequestTwo
import me.hexalite.liteware.network.raknet.protocol.send
import me.hexalite.liteware.network.session.NetworkPlayerSession
import me.hexalite.liteware.network.session.findCurrentSessionOrNull

fun isIPv6(hostname: String): Boolean = hostname.contains(':')

@OptIn(ExperimentalUnsignedTypes::class, ExperimentalIoApi::class)
internal suspend fun LitewareNetworkBootstrap.handleConnections() {
    onEachPacket<OpenConnectionRequestOne> { (_, protocol, mtu, details) ->
        LitewareAPI.logger.info("Received a connection (1). ($protocol/$mtu, ${details.clientAddress})")
        val reply = OpenConnectionReplyOne(Magic, details.server.guid, mtu, details)

        if(rakNet.findCurrentSessionOrNull(details.clientAddress) == null) {
            val session = NetworkPlayerSession(protocol, details.clientAddress, mtu, rakNet)
            rakNet.sessions.emit(session)
        }

        rakNet.send(reply)
    }
    onEachPacket<OpenConnectionRequestTwo> { (_, _, mtu, _, details) ->
        LitewareAPI.logger.info("Received a connection (2). ($mtu, ${details.clientAddress})")
        val session = rakNet.findCurrentSessionOrNull(details.clientAddress)
        if(session != null) {
            LitewareAPI.logger.info("Connection session found (2). ($mtu, ${details.clientAddress})")
            val reply = OpenConnectionReplyTwo(Magic, details.server.guid, details.clientAddress, mtu, details)
            rakNet.send(reply)
        }
    }
}
