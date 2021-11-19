@file:JvmName("CustomPacketHandler")

package me.hexalite.liteware.network.handlers

import me.hexalite.liteware.api.logging.logger
import me.hexalite.liteware.network.bootstrap.LitewareNetworkBootstrap
import me.hexalite.liteware.network.codec.RakNetPacketCodec
import me.hexalite.liteware.network.codec.decode
import me.hexalite.liteware.network.raknet.protocol.RakNetPacketDetails
import me.hexalite.liteware.network.raknet.protocol.custom.EncapsulatedPacket
import me.hexalite.liteware.network.raknet.protocol.custom.FrameSet
import me.hexalite.liteware.network.raknet.protocol.custom.GamePacket
import me.hexalite.liteware.network.raknet.protocol.custom.UnknownPacket
import me.hexalite.liteware.network.raknet.protocol.findRakNetPacketCodec
import me.hexalite.liteware.network.raknet.protocol.findRakNetPacketId

fun Byte.hex(): String {
    val hexString = Integer.toHexString(this.toInt() and 0xFF)
    return if (hexString.length % 2 != 0) "0x0${hexString.uppercase()}" else "0x${hexString.uppercase()}"
}

internal fun LitewareNetworkBootstrap.handleCustomPackets() {
    val gamePacketId = findRakNetPacketId<GamePacket>()
    onEachPacket<FrameSet> { (_, messages, details) ->
        for (packet in messages) {
            val packetId = packet.data.readByte()
            val codec = findRakNetPacketCodec(packetId)
            when {
                codec !is UnknownPacket.Codec -> handleRakNetPacket(packetId, codec, packet, details)
                packetId == gamePacketId -> handleGamePacket(packetId, packet, details)
                else -> logger.info("Unknown custom packet received, id: ${packetId.hex()} - ${details.clientAddress}")
            }
        }
    }
    onEachGameNetworkPacket {
        logger.info("Minecraft packet received successfully: ${it.gamePacket.data}")
    }
}

private suspend fun LitewareNetworkBootstrap.handleRakNetPacket(
    packetId: Byte,
    codec: RakNetPacketCodec<*>,
    packet: EncapsulatedPacket,
    details: RakNetPacketDetails
) {
    logger.debug("Custom rak net packet received, id: ${packetId.hex()} - ${details.clientAddress}")
    rakNet.rakNetPackets.emit(codec.decode(packet.data, details))
}

@OptIn(ExperimentalUnsignedTypes::class)
private suspend fun LitewareNetworkBootstrap.handleGamePacket(
    packetId: Byte,
    packet: EncapsulatedPacket,
    details: RakNetPacketDetails
) {
    logger.debug("Custom minecraft packet received, id: ${packetId.hex()} - ${details.clientAddress}")
/*    val codec = findMinecraftPacketCodec(packet.data.readUByte().toByte())
    val gamePacket = GamePacket(codec.decode(packet.data), details)
    rakNet.packets.emit(NetworkMinecraftPacket(gamePacket, details.clientAddress))*/
}

