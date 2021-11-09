package me.hexalite.liteware.network.packets

import io.ktor.network.sockets.*
import me.hexalite.liteware.network.annotations.RakNetPacketInfo

@RakNetPacketInfo(0x08)
data class OpenConnectionReply2(override val datagram: Datagram): RakNetPacket
