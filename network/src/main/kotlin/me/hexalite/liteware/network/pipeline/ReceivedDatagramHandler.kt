package me.hexalite.liteware.network.pipeline

import me.hexalite.liteware.api.logging.logger
import me.hexalite.liteware.network.raknet.protocol.RakNetPacket
import me.hexalite.liteware.network.raknet.protocol.connection.OpenConnectionRequestOne
import me.hexalite.liteware.network.raknet.protocol.connection.OpenConnectionRequestTwo
import me.hexalite.liteware.network.raknet.protocol.custom.RakNetCustomPacket
import me.hexalite.liteware.network.raknet.protocol.decodeRakNetPacket
import me.hexalite.liteware.network.raknet.protocol.ping.UnconnectedPing
import me.hexalite.liteware.network.session.sessions
import me.hexalite.liteware.network.udp.UDPServerEvent.DatagramReceived

object ReceivedDatagramHandler : PipelineExecutor<DatagramReceived> {

    override suspend fun middleware(event: DatagramReceived) {
        val (datagram, server) = event
        if (datagram.address in server.info.blockedAddresses) {
            return
        }
        runCatching {
            val packet = datagram.packet
            val packetId = packet.readByte()
            val rakNetPacket = decodeRakNetPacket(packetId, datagram, server)

            // Check if the packet needs authentication or not.
            if (rakNetPacket.requiresSession()) {
                // If the packet needs authentication, then check if the session is authenticated or not.
                server.sessions.find(datagram.address) ?: return
            } else if (rakNetPacket is RakNetCustomPacket) {
                // Let's check whether the packet is available for this session.
                val session = server.sessions.find(datagram.address)
                    ?: // If the session is not found, then the packet isn't really a custom packet.
                    return
                // If the session is found, then the packet is really a custom packet.
                // Let's handle the packet by emitting it to the Flow so that the packet handler can handle it.
                return server.rakNetPackets.emit(rakNetPacket)
            }

            server.rakNetPackets.emit(rakNetPacket)
        }.onFailure {
            logger.error("Failed to handle RakNet packet", it)
        }
    }

}

private fun RakNetPacket.requiresSession(): Boolean =
    !(this is UnconnectedPing || this is OpenConnectionRequestOne || this is OpenConnectionRequestTwo)