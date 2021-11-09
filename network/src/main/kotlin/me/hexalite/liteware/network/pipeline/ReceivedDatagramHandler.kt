package me.hexalite.liteware.network.pipeline

import me.hexalite.liteware.network.findRakNetPacket
import me.hexalite.liteware.network.packets.*
import me.hexalite.liteware.network.session.findCurrentSessionOrNull
import me.hexalite.liteware.network.udp.UDPServerEvent.DatagramReceived

object ReceivedDatagramHandler : PipelineExecutor<DatagramReceived> {

    override suspend fun middleware(event: DatagramReceived) {
        val (datagram, server) = event
        if (datagram.address in server.info.blockedAddresses) {
            return
        }

        val packet = datagram.packet
        val packetId = packet.readByte()
        val rakNetPacket = findRakNetPacket(packetId, datagram)

        // Check if the packet needs authentication or not.
        if (rakNetPacket.requiresSession()) {
            // If the packet needs authentication, then check if the session is authenticated or not.
            server.findCurrentSessionOrNull(datagram.address) ?: return
        }
        else if (rakNetPacket is UnknownPacket) {
            // If the packet is not a RakNet packet, then it is a custom packet.
            // Let's check whether the packet is a custom packet or not.
            val session = server.findCurrentSessionOrNull(datagram.address)
                ?: // If the session is not found, then the packet isn't a custom packet.
                return
            // If the session is found, then the packet is really a custom packet.
            // Let's handle the packet by emitting it to the Flow so that the packet handler can handle it.
            return server.rakNetPackets.emit(rakNetPacket)
        }

        server.rakNetPackets.emit(rakNetPacket)
    }

}

private fun RakNetPacket.requiresSession(): Boolean =
    !(this is UnconnectedPing || this is OpenConnectionRequest1 || this is OpenConnectionRequest2)