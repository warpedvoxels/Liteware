package me.hexalite.liteware.network.packets

import io.ktor.network.sockets.*
import me.hexalite.liteware.network.annotations.RakNetPacketInfo

@RakNetPacketInfo(0xfe.toByte())
data class GamePacket(override val datagram: Datagram): RakNetPacket
