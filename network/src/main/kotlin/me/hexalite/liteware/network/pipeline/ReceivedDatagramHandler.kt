package me.hexalite.liteware.network.pipeline

import me.hexalite.liteware.network.raknet.RakNet
import me.hexalite.liteware.network.session.handleLogin
import me.hexalite.liteware.network.udp.UDPServerEvent.DatagramReceived

object ReceivedDatagramHandler: PipelineExecutor<DatagramReceived> {

    override suspend fun middleware(event: DatagramReceived) {
        val (datagram, server) = event
        if(datagram.address in server.info.blockedAddresses) {
            return
        }
        val packet = datagram.packet

        when(val packetId = packet.readByte()) {
            RakNet.Packets.OPEN_CONNECTION_REQUEST_1 -> return handleLogin(event)
        }
    }

}