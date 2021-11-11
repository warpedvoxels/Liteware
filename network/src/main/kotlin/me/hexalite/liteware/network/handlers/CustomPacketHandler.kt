@file:JvmName("CustomPacketHandler")

package me.hexalite.liteware.network.handlers

import me.hexalite.liteware.api.logging.logger
import me.hexalite.liteware.network.bootstrap.LitewareNetworkBootstrap
import me.hexalite.liteware.network.raknet.protocol.custom.RakNetCustomPacket

@OptIn(ExperimentalUnsignedTypes::class)
internal fun LitewareNetworkBootstrap.handleCustomPackets() =
    onEachPacket<RakNetCustomPacket> { (_, msgs, _, details) ->
        msgs.forEach {
            val data = it.data
            val packetId = Integer.toHexString(data.readByte().toInt())
            logger.info("Custom packet received, id: $packetId - ${details.clientAddress}")
        }
    }
