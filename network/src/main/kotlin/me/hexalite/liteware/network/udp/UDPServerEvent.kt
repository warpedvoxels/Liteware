package me.hexalite.liteware.network.udp

import io.ktor.network.sockets.*
import me.hexalite.liteware.network.raknet.LitewareRakNetServer

sealed interface UDPServerEvent<T> {
    val data: T

    object Started : UDPServerEvent<Unit> {
        override val data = Unit
    }

    object Closed : UDPServerEvent<Unit> {
        override val data = Unit
    }

    data class DatagramReceived(override val data: Datagram, val rakNetServer: LitewareRakNetServer) :
        UDPServerEvent<Datagram>
}