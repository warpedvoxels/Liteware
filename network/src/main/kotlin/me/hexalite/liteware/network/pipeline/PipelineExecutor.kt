package me.hexalite.liteware.network.pipeline

import me.hexalite.liteware.network.udp.UDPServerEvent
import me.hexalite.liteware.network.utils.Executor

fun interface PipelineExecutor<T: UDPServerEvent<*>>: Executor<T>

