package me.hexalite.liteware.network.packets

import io.ktor.network.sockets.*

/**
 * Represents a packet sent by the client to the RakNet server.
 * Learn more about RakNet at: [https://wiki.vg/Raknet_Protocol]
 */
interface RakNetPacket {
    val datagram: Datagram
}

