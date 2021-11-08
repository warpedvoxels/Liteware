package me.hexalite.liteware.network.raknet

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import me.hexalite.liteware.network.pipeline.ReceivedDatagramHandler
import me.hexalite.liteware.network.session.NetworkPacket
import me.hexalite.liteware.network.session.NetworkPlayerSession
import me.hexalite.liteware.network.udp.LitewareUDPServer

class LitewareRakNetServer(val info: RakNetServerInfo) {

    private val server = LitewareUDPServer(this, info)

    val sessions = MutableSharedFlow<NetworkPlayerSession>()

    val packets = MutableSharedFlow<NetworkPacket>()

    fun start() {
        val pipelineJob = SupervisorJob()
        val pipelineScope = CoroutineScope(pipelineJob + Dispatchers.IO)
        server.pipeline(pipelineScope, ReceivedDatagramHandler)
        server.start(pipelineScope, pipelineJob)
    }

    suspend fun close() {
        server.close()
    }

}