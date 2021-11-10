package me.hexalite.liteware.network.bootstrap

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import me.hexalite.liteware.api.LitewareAPI
import me.hexalite.liteware.network.LitewareRakNetServer
import me.hexalite.liteware.network.RakNetServerInfo
import me.hexalite.liteware.network.handlers.initializeHandlers

class LitewareNetworkBootstrap(val flags: List<BootstrapFlag> = emptyList(), val rakNetServerInfo: RakNetServerInfo) {

    val rakNet = LitewareRakNetServer(rakNetServerInfo)

    private val handlersJob = SupervisorJob()
    val handlersScope = CoroutineScope(handlersJob + rakNetServerInfo.dispatcher)

    suspend fun boot() {
        // TODO: Run actions for each flag.
        initializeHandlers()
        rakNet.start()
        LitewareAPI.logger.info("Running server at udp://${rakNetServerInfo.hostname}:${rakNetServerInfo.port}")
    }

    suspend fun close() {
        handlersJob.cancel()
        rakNet.close()
    }

}
