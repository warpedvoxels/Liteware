@file:JvmName("CustomPacketHandler")

package me.hexalite.liteware.network.handlers

import me.hexalite.liteware.api.LitewareAPI
import me.hexalite.liteware.network.bootstrap.LitewareNetworkBootstrap
import me.hexalite.liteware.network.raknet.protocol.custom.RakNetCustomPacket

private fun Byte.toHexadecimal() = "%02X".format(this)

@OptIn(ExperimentalUnsignedTypes::class)
internal fun LitewareNetworkBootstrap.handleCustomPackets() = onEachPacket<RakNetCustomPacket> { (_, msgs) ->
    msgs.forEach {
        val data = it.data
        val packetId = data.readByte()
        LitewareAPI.logger.info("Custom packet received, id: $packetId (${packetId.toHexadecimal()})")
    }
}
