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
import me.hexalite.liteware.network.raknet.protocol.kick.ConnectionBanned
import me.hexalite.liteware.network.raknet.protocol.login.ConnectionRequest
import me.hexalite.liteware.network.raknet.protocol.login.NewIncomingConnection
import me.hexalite.liteware.network.raknet.protocol.send
import me.hexalite.liteware.network.session.NetworkPlayerSession
import me.hexalite.liteware.network.session.sessions

fun isIPv6(hostname: String): Boolean = hostname.contains(':')

@OptIn(ExperimentalUnsignedTypes::class, ExperimentalIoApi::class)
internal suspend fun LitewareNetworkBootstrap.handleConnections() {
    onEachPacket<OpenConnectionRequestOne> { (_, protocol, mtu, details) ->
        if(details.clientAddress in rakNet.info.blockedAddresses) {
            val reply = ConnectionBanned(Magic, rakNet.guid, details)
            rakNet.send(reply)
        }

        LitewareAPI.logger.info("Received a connection (1). ($protocol/$mtu, ${details.clientAddress})")
        val reply = OpenConnectionReplyOne(Magic, details.server.guid, mtu, details)
        rakNet.send(reply)

        if(rakNet.sessions.find(details.clientAddress) == null) {
            val session = NetworkPlayerSession(protocol, details.clientAddress, mtu, rakNet)
            rakNet.sessions.create(session)
        }
    }
    onEachPacket<OpenConnectionRequestTwo> { (_, _, mtu, _, details) ->
        if(details.clientAddress in rakNet.info.blockedAddresses) {
            val reply = ConnectionBanned(Magic, rakNet.guid, details)
            rakNet.send(reply)
        }

        LitewareAPI.logger.info("Received a connection (2). ($mtu, ${details.clientAddress})")
        val session = rakNet.sessions.find(details.clientAddress)

        if(session != null) {
            LitewareAPI.logger.info("Connection session found (2). ($mtu, ${details.clientAddress})")
            val reply = OpenConnectionReplyTwo(Magic, details.server.guid, details.clientAddress, mtu, details)
            rakNet.send(reply)
        }
    }
    onEachPacket<ConnectionRequest> { (clientGuid, time, details) ->
        if(details.clientAddress in rakNet.info.blockedAddresses) {
            val reply = ConnectionBanned(Magic, rakNet.guid, details)
            rakNet.send(reply)
        }
        LitewareAPI.logger.info("Received a connection (final).")
    }
    onEachPacket<NewIncomingConnection> { (serverAddress, internalAddress, details) ->
        if(details.clientAddress in rakNet.info.blockedAddresses) {
            val reply = ConnectionBanned(Magic, rakNet.guid, details)
            rakNet.send(reply)
        }
        LitewareAPI.logger.info("Received a connection (incoming).")
    }
}
