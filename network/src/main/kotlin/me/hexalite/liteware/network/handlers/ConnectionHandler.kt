@file:JvmName("LoginHandler")

package me.hexalite.liteware.network.handlers

import io.ktor.utils.io.core.*
import kotlinx.coroutines.ObsoleteCoroutinesApi
import me.hexalite.liteware.api.logging.logger
import me.hexalite.liteware.network.bootstrap.LitewareNetworkBootstrap
import me.hexalite.liteware.network.datatypes.Magic
import me.hexalite.liteware.network.datatypes.RakNetReliability
import me.hexalite.liteware.network.raknet.constants.RakNet.MAXIMUM_MTU_SIZE
import me.hexalite.liteware.network.raknet.constants.RakNet.MINIMUM_MTU_SIZE
import me.hexalite.liteware.network.raknet.constants.RakNet.RAKNET_PROTOCOL_VERSION
import me.hexalite.liteware.network.raknet.protocol.connection.OpenConnectionReplyOne
import me.hexalite.liteware.network.raknet.protocol.connection.OpenConnectionReplyTwo
import me.hexalite.liteware.network.raknet.protocol.connection.OpenConnectionRequestOne
import me.hexalite.liteware.network.raknet.protocol.connection.OpenConnectionRequestTwo
import me.hexalite.liteware.network.raknet.protocol.kick.ConnectionBanned
import me.hexalite.liteware.network.raknet.protocol.kick.DisconnectNotification
import me.hexalite.liteware.network.raknet.protocol.kick.IncompatibleProtocolVersion
import me.hexalite.liteware.network.raknet.protocol.login.ConnectionRequest
import me.hexalite.liteware.network.raknet.protocol.login.ConnectionRequestAccepted
import me.hexalite.liteware.network.raknet.protocol.login.NewIncomingConnection
import me.hexalite.liteware.network.raknet.protocol.send
import me.hexalite.liteware.network.raknet.protocol.sendFramed
import me.hexalite.liteware.network.session.NetworkPlayerSession
import me.hexalite.liteware.network.session.sessions

@OptIn(ExperimentalUnsignedTypes::class, ExperimentalIoApi::class, ObsoleteCoroutinesApi::class)
internal suspend fun LitewareNetworkBootstrap.handleConnections() {
    onEachPacket<OpenConnectionRequestOne> { (_, protocol, mtu, details) ->
        if (details.clientAddress in rakNet.info.blockedAddresses) {
            val reply = ConnectionBanned(Magic, rakNet.guid, details)
            return@onEachPacket rakNet.send(reply)
        }
        if (mtu !in MINIMUM_MTU_SIZE..MAXIMUM_MTU_SIZE) {
            logger.info("Connection rejected for ${details.clientAddress}; MTU criteria not met ($mtu -> $MINIMUM_MTU_SIZE..$MAXIMUM_MTU_SIZE).")
            val reply = ConnectionBanned(Magic, rakNet.guid, details)
            return@onEachPacket rakNet.send(reply)
        }
        if (protocol.toByte() != RAKNET_PROTOCOL_VERSION) {
            logger.info("Connection rejected for ${details.clientAddress}; protocol version mismatch (${protocol.toByte()} != $RAKNET_PROTOCOL_VERSION).")
            val reply = IncompatibleProtocolVersion(RAKNET_PROTOCOL_VERSION, Magic, rakNet.guid, details)
            return@onEachPacket rakNet.send(reply)
        }
        logger.info("Received a connection (1). ($protocol/$mtu, ${details.clientAddress})")
        val reply = OpenConnectionReplyOne(Magic, details.server.guid, mtu, details)
        rakNet.send(reply)

        if (rakNet.sessions.find(details.clientAddress) == null) {
            val session = NetworkPlayerSession(protocol, details.clientAddress, mtu, rakNet)
            rakNet.sessions.create(session)
        }
    }
    onEachPacket<OpenConnectionRequestTwo> { (_, _, mtu, guid, details) ->
        val session = rakNet.sessions.find(details.clientAddress)
        if (session != null) {
            rakNet.sessions.create(session.copy(guid = guid))
            logger.info("Connection session found (2). ($mtu, $guid, ${details.clientAddress})")
            val reply = OpenConnectionReplyTwo(Magic, details.server.guid, details.clientAddress, mtu, details)
            rakNet.send(reply)
        }
    }
    onEachPacket<ConnectionRequest> { (guid, time, useSecurity, details) ->
        if (useSecurity) {
            logger.info("Connection rejected for ${details.clientAddress}; security is not supported.")
            val reply = ConnectionBanned(Magic, rakNet.guid, details)
            rakNet.sessions.delete(details.clientAddress)
            return@onEachPacket rakNet.send(reply)
        }
        val session = rakNet.sessions.find(details.clientAddress)
        if (session != null) {
            if (guid != session.guid) {
                logger.info("Connection rejected for ${details.clientAddress}; GUID criteria not met ($guid != ${session.guid}).")
                val reply = ConnectionBanned(Magic, rakNet.guid, details)
                rakNet.sessions.delete(details.clientAddress)
                return@onEachPacket rakNet.send(reply)
            }
            logger.info("Accepting final connection for ${details.clientAddress}.")
            val reply = ConnectionRequestAccepted(details.clientAddress, time, System.currentTimeMillis(), details)
            rakNet.sendFramed(reply, RakNetReliability.RELIABLE, 0)
        }
    }
    onEachPacket<NewIncomingConnection> { (serverAddress, internalAddress, details) ->
        logger.info("Successfully started a final session for ${details.clientAddress}.")
    }
    onEachPacket<DisconnectNotification> { (details) ->
        logger.info("Connection terminated for ${details.clientAddress}.")
        rakNet.sessions.delete(details.clientAddress)
    }
}
