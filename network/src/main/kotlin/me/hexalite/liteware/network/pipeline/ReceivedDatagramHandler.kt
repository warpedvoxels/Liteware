package me.hexalite.liteware.network.pipeline

import me.hexalite.liteware.api.logging.logger
import me.hexalite.liteware.network.codec.decode
import me.hexalite.liteware.network.raknet.protocol.RakNetPacket
import me.hexalite.liteware.network.raknet.protocol.RakNetPacketDetails
import me.hexalite.liteware.network.raknet.protocol.connection.OpenConnectionRequestOne
import me.hexalite.liteware.network.raknet.protocol.connection.OpenConnectionRequestTwo
import me.hexalite.liteware.network.raknet.protocol.custom.FrameSet
import me.hexalite.liteware.network.raknet.protocol.custom.UnknownPacket
import me.hexalite.liteware.network.raknet.protocol.findRakNetPacketCodec
import me.hexalite.liteware.network.raknet.protocol.ping.UnconnectedPing
import me.hexalite.liteware.network.session.sessions
import me.hexalite.liteware.network.udp.UDPServerEvent.DatagramReceived
import me.hexalite.liteware.protocol.packet.hex
import java.net.InetSocketAddress

object ReceivedDatagramHandler : PipelineExecutor<DatagramReceived> {

    override suspend fun middleware(event: DatagramReceived) {
        val (datagram, server) = event
        if (datagram.address in server.info.blockedAddresses) {
            return
        }
        try {
            val packet = datagram.packet
            val packetId = packet.readByte()

            val rakNetPacket = findRakNetPacketCodec(packetId)
                .decode(packet, RakNetPacketDetails(server, datagram.address as InetSocketAddress))

            // Check if the packet needs authentication or not.
            if (rakNetPacket.requiresSession()) {
                // If the packet needs authentication, then check if the session is authenticated or not.
                server.sessions.find(datagram.address) ?: return
            } else if (rakNetPacket is FrameSet) {
                // Let's check whether the packet is available for this session.
                server.sessions.find(datagram.address)
                    ?: // If the session is not found, then the packet isn't really a custom packet.
                    return
            } else if (rakNetPacket is UnknownPacket) {
                return logger.info("An unknown packet has been received (${packetId.hex()}).")
            }

            server.rakNetPackets.emit(rakNetPacket)
        } catch (error: Exception) {
            logger.error("Failed to handle RakNet packet", error)
        }
    }

}

private fun RakNetPacket.requiresSession(): Boolean =
    !(this is UnconnectedPing || this is OpenConnectionRequestOne || this is OpenConnectionRequestTwo)