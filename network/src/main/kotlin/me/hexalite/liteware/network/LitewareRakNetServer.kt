package me.hexalite.liteware.network

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableSharedFlow
import me.hexalite.liteware.network.cache.CacheView
import me.hexalite.liteware.network.pipeline.ReceivedDatagramHandler
import me.hexalite.liteware.network.raknet.protocol.RakNetPacket
import me.hexalite.liteware.network.raknet.protocol.custom.NetworkMinecraftPacket
import me.hexalite.liteware.network.udp.LitewareUDPServer
import java.util.*

class LitewareRakNetServer(val info: RakNetServerInfo) {

    val udp = LitewareUDPServer(this, info)

    val guid = UUID.randomUUID().leastSignificantBits

    val cache = CacheView(info.internalCache)

    val packets = MutableSharedFlow<NetworkMinecraftPacket>()

    val rakNetPackets = MutableSharedFlow<RakNetPacket>()

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