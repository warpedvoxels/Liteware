@file:JvmName("LoginHandler")

package me.hexalite.liteware.network.handlers

import io.ktor.network.sockets.*
import io.ktor.util.network.*
import io.ktor.utils.io.core.*
import me.hexalite.liteware.network.RakNet
import me.hexalite.liteware.network.bootstrap.LitewareNetworkBootstrap
import me.hexalite.liteware.network.findRakNetPacketId
import me.hexalite.liteware.network.packets.OpenConnectionReply1
import me.hexalite.liteware.network.packets.OpenConnectionReply2
import me.hexalite.liteware.network.packets.OpenConnectionRequest1
import me.hexalite.liteware.network.packets.OpenConnectionRequest2
import me.hexalite.liteware.network.session.NetworkPlayerSession
import me.hexalite.liteware.network.session.findCurrentSessionOrNull
import me.hexalite.liteware.network.utils.writeAddress
import me.hexalite.liteware.protocol.ProtocolVersion
import me.hexalite.liteware.protocol.datatypes.writeBoolean

fun isIPv6(hostname: String): Boolean = hostname.contains(':')

@OptIn(ExperimentalUnsignedTypes::class, ExperimentalIoApi::class)
internal suspend fun LitewareNetworkBootstrap.handleConnections() {
    onEachPacket<OpenConnectionRequest1> { (datagram) ->
        println("connection 1")
        val packet = datagram.packet

        val protocolVersion = ProtocolVersion[packet.readUByte().toInt()]
        val mtu = (packet.remaining + 1 + 16 + 1 + (if (isIPv6(datagram.address.hostname)) 40 else 20)).toShort()

        // Verify if is not a duplicated session.
        if(protocolVersion !in rakNet.info.supportedProtocolVersions) {
            println("todo -> unsupported protocol")
            return@onEachPacket
        }

        val reply = OpenConnectionReply1(
            Datagram(
                buildPacket {
                    writeByte(findRakNetPacketId<OpenConnectionReply1>())
                    writeFully(RakNet.UNCONNECTED_MAGIC)
                    writeLong(rakNet.guid)
                    writeShort(mtu)
                },
                datagram.address
            )
        )

        if(rakNet.findCurrentSessionOrNull(datagram.address) == null) {
            val session = NetworkPlayerSession(protocolVersion!!, datagram.address, mtu, rakNet)
            rakNet.sessions.emit(session)
        }

        rakNet.send(reply)
    }
    onEachPacket<OpenConnectionRequest2> { (datagram) ->
        println("connection 2")
        val packet = datagram.packet
        val session = rakNet.findCurrentSessionOrNull(datagram.address)

        if(session == null) {
            println("Invalid session order")
            return@onEachPacket
        }

        val reply = OpenConnectionReply2(
            Datagram(
                buildPacket {
                    writeByte(findRakNetPacketId<OpenConnectionReply1>())
                    writeFully(RakNet.UNCONNECTED_MAGIC)
                    writeAddress(datagram)
                    writeShort(session.mtu)
                    writeBoolean(false)
                },
                datagram.address
            )
        )

        rakNet.send(reply)
    }
}
