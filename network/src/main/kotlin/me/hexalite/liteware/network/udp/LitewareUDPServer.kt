package me.hexalite.liteware.network.udp

import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import me.hexalite.liteware.network.pipeline.PipelineExecutor
import me.hexalite.liteware.network.raknet.LitewareRakNetServer
import me.hexalite.liteware.network.raknet.RakNetServerInfo
import java.net.InetSocketAddress

class LitewareUDPServer(private val rakNetServer: LitewareRakNetServer, val rakNetServerInfo: RakNetServerInfo) {
    val ktor by lazy {
        with(rakNetServerInfo) {
            aSocket(ActorSelectorManager(dispatcher)).udp().bind(InetSocketAddress(hostname, port.toInt()))
        }
    }

    private var job: Job? = null
    val events = MutableSharedFlow<UDPServerEvent<*>>()

    inline fun <reified T : UDPServerEvent<*>> pipeline(launchIn: CoroutineScope, pipeline: PipelineExecutor<T>) {
        events
            .filterIsInstance<T>()
            .onEach { pipeline.middleware(it) }
            .launchIn(launchIn)
    }

    fun start(scope: CoroutineScope, job: Job) = scope.also {
        this.job = job
        it.launch {
            events.emit(UDPServerEvent.Started)
            ktor.incoming.consumeEach { datagram ->
                events.emit(UDPServerEvent.DatagramReceived(datagram, rakNetServer))
            }
        }
    }

    suspend fun close() {
        this.job?.cancel()
        events.emit(UDPServerEvent.Closed)
    }
}