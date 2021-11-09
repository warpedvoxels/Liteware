package me.hexalite.liteware.network.packets

import io.ktor.network.sockets.*
import me.hexalite.liteware.network.annotations.RakNetPacketInfo

/**
 * Represents an unknown packet.
 *
 * @param data The data of the packet.
 */
@RakNetPacketInfo(-1)
data class UnknownPacket(override val datagram: Datagram): RakNetPacket
