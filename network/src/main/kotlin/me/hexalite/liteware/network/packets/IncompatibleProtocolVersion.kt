package me.hexalite.liteware.network.packets

import io.ktor.network.sockets.*
import me.hexalite.liteware.network.annotations.RakNetPacketInfo

@RakNetPacketInfo(0x19)
data class IncompatibleProtocolVersion(override val datagram: Datagram): RakNetPacket
