package me.hexalite.liteware.network

import io.ktor.network.sockets.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import me.hexalite.liteware.network.packets.RakNetPacket
import me.hexalite.liteware.network.pipeline.ReceivedDatagramHandler
import me.hexalite.liteware.network.session.NetworkPacket
import me.hexalite.liteware.network.session.NetworkPlayerSession
import me.hexalite.liteware.network.udp.LitewareUDPServer
import java.util.*

class LitewareRakNetServer(val info: RakNetServerInfo) {

    val udp = LitewareUDPServer(this, info)

    val guid = UUID.randomUUID().leastSignificantBits

    val sessions = MutableSharedFlow<NetworkPlayerSession>()

    val packets = MutableSharedFlow<NetworkPacket>()

    val rakNetPackets = MutableSharedFlow<RakNetPacket>()

    suspend fun send(packet: RakNetPacket) = udp.ktor.send(Datagram(packet.datagram.packet, packet.datagram.address))

    fun start() {
        val pipelineJob = SupervisorJob()
        val pipelineScope = CoroutineScope(pipelineJob + Dispatchers.IO)
        udp.pipeline(pipelineScope, ReceivedDatagramHandler)
        udp.start(pipelineScope, pipelineJob)
    }

    suspend fun close() {
        udp.close()
    }

}