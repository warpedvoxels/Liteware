@file:JvmName("LoginHandler")
package me.hexalite.liteware.network.session

import io.ktor.network.sockets.*
import io.ktor.util.network.*
import io.ktor.utils.io.core.*
import me.hexalite.liteware.network.udp.UDPServerEvent
import me.hexalite.liteware.protocol.ProtocolVersion

private fun isIPv6(datagram: Datagram): Boolean = datagram.address.hostname.contains(':')

@OptIn(ExperimentalUnsignedTypes::class)
internal suspend fun handleLogin(datagramReceived: UDPServerEvent.DatagramReceived) {
    val (datagram, server) = datagramReceived
    val packet = datagram.packet

    val protocolVersion = ProtocolVersion[packet.readUByte().toInt()]
    val mtu = packet.remaining.toInt() + 1 + 16 + 1 + (if(isIPv6(datagram)) 40 else 20)

    // Verify if is not a duplicated session.
    val previousSessionExist = server.getCurrentSessionOrNull(datagram.address) != null
    when {
        previousSessionExist -> {
            // kick
            return
        }
        protocolVersion !in server.info.supportedProtocolVersions -> {
            // kick
            return
        }
    }

    val session = NetworkPlayerSession(protocolVersion!!, datagram.address, mtu, server)
    server.sessions.emit(session)
}
