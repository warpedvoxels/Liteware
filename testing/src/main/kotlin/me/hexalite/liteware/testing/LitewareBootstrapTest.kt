package me.hexalite.liteware.testing

import me.hexalite.liteware.network.RakNetServerInfo
import me.hexalite.liteware.network.bootstrap.LitewareNetworkBootstrap
import me.hexalite.liteware.network.cache.buildServerDefaultCache

// for testing purposes only
suspend fun main() {
    val bootstrap = LitewareNetworkBootstrap(
        emptyList(),
        RakNetServerInfo(
            internalCache = buildServerDefaultCache(),
            hostname = "localhost",
            port = 19132
        )
    )
    bootstrap.handlePings()
    bootstrap.boot()

    Thread {
        while (true) {
            println("-> ${readLine()}")
        }
    }.run()
}