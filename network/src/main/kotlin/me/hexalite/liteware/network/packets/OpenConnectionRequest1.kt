package me.hexalite.liteware.network.packets

import io.ktor.network.sockets.*
import me.hexalite.liteware.network.annotations.RakNetPacketInfo

@RakNetPacketInfo(0x05)
data class OpenConnectionRequest1(override val datagram: Datagram): RakNetPacket
