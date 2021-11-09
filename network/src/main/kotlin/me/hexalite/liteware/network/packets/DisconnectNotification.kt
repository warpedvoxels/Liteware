package me.hexalite.liteware.network.packets

import io.ktor.network.sockets.*
import me.hexalite.liteware.network.annotations.RakNetPacketInfo

@RakNetPacketInfo(id = 0x15)
data class DisconnectNotification(override val datagram: Datagram): RakNetPacket
