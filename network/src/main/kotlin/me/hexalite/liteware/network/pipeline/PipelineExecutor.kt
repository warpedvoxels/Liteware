package me.hexalite.liteware.network.pipeline

import me.hexalite.liteware.network.udp.UDPServerEvent

interface PipelineExecutor<T: UDPServerEvent<*>> {
    suspend fun middleware(event: T)
}
