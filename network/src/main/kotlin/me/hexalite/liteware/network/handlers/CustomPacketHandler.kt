@file:JvmName("CustomPacketHandler")

package me.hexalite.liteware.network.handlers

import io.ktor.utils.io.core.*
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
import me.hexalite.liteware.protocol.codec.decode
import me.hexalite.liteware.protocol.compression.Zlib
import me.hexalite.liteware.protocol.datatypes.readUVarInt
import me.hexalite.liteware.protocol.packet.findMinecraftPacketCodecOrNull
import me.hexalite.liteware.protocol.packet.hex

private val gamePacketId = findRakNetPacketId<GamePacket>()

internal fun LitewareNetworkBootstrap.handleCustomPackets() {
    onEachPacket<FrameSet> { (_, messages, details) ->
        for (packet in messages) {
            when (val packetId = packet.data.readByte()) {
                gamePacketId -> handleGamePacket(packet, details)
                else -> {
                    val codec = findRakNetPacketCodec(packetId)
                    if (codec !is UnknownPacket.Codec) {
                        handleRakNetPacket(packetId, codec, packet, details)
                        continue
                    }
                    logger.info("Unknown custom packet received, id: ${packetId.hex()} - ${details.clientAddress}")
                }
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

// bad code that need to be fixed
@OptIn(ExperimentalUnsignedTypes::class)
private suspend fun LitewareNetworkBootstrap.handleGamePacket(packet: EncapsulatedPacket, details: RakNetPacketDetails) {
    val data = Zlib.Packet.decompress(packet.data)
    while (data.remaining > 0) {
        val length = data.readUVarInt().toInt()
        logger.debug("Received a game packet from ${details.clientAddress}, length: $length")
        if (length > data.remaining) {
            return logger.info("[${details.clientAddress} » NETWORK] Length of packet is greater than remaining data. ($length > ${data.remaining})")
        }
        val data = ByteReadPacket(data.readBytes(length))
        val header = data.readUVarInt().toInt()
        val id = header and 0x3ff

        logger.debug("Custom minecraft packet received, id: ${id.hex()} - ${details.clientAddress}")
        val codec = findMinecraftPacketCodecOrNull(id) ?: return logger.info("Unknown minecraft packet with id '${id.hex()}'")
        println(codec.decode(data))
    }
/*    val gamePacket = GamePacket(codec.decode(data), details)
    rakNet.packets.emit(NetworkMinecraftPacket(gamePacket, details.clientAddress))*/
}


