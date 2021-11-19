@file:JvmName("PingHandler")
package me.hexalite.liteware.testing

import me.hexalite.liteware.api.logging.logger
import me.hexalite.liteware.network.bootstrap.LitewareNetworkBootstrap
import me.hexalite.liteware.network.datatypes.Magic
import me.hexalite.liteware.network.handlers.onEachPacket
import me.hexalite.liteware.network.raknet.protocol.ping.UnconnectedPing
import me.hexalite.liteware.network.raknet.protocol.ping.UnconnectedPong
import me.hexalite.liteware.network.raknet.protocol.send
import me.hexalite.liteware.protocol.ProtocolVersion

internal suspend fun LitewareNetworkBootstrap.handlePings() = onEachPacket<UnconnectedPing> { (time, _, _, details) ->
    logger.info("Received a ping from ${details.clientAddress}")
    // todo: configurable motds
    val v = ProtocolVersion.Bedrock_1_17_40_to_1_17_41
    val serverId = "MCPE;Liteware Server;$v;1.17.41;0;10;13253860822328930865;Unavailable;Survival;1;19132;19133;"
    val reply = UnconnectedPong(time, details.server.guid, Magic, serverId, details)
    rakNet.send(reply)
}
