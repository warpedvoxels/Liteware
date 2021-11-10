package me.hexalite.liteware.network.raknet.protocol

import io.ktor.util.network.*
import me.hexalite.liteware.network.LitewareRakNetServer

/**
 * Learn more about RakNet at:
 * [https://wiki.vg/Raknet_Protocol]
 */
interface RakNetPacket {
    val details: RakNetPacketDetails
}

data class RakNetPacketDetails(
    val server: LitewareRakNetServer,
    val clientAddress: NetworkAddress
)
