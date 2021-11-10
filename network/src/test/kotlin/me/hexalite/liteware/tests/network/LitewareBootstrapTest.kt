package me.hexalite.liteware.tests.network

import me.hexalite.liteware.network.RakNetServerInfo
import me.hexalite.liteware.network.bootstrap.LitewareNetworkBootstrap
import me.hexalite.liteware.network.handlers.handlePings

// for testing purposes only
suspend fun main() {
    val bootstrap = LitewareNetworkBootstrap(emptyList(), RakNetServerInfo(hostname = "localhost", port = 19132))
    bootstrap.handlePings()
    bootstrap.boot()

    Thread {
        while (true) {
            println("-> ${readLine()}")
        }
    }.run()
}