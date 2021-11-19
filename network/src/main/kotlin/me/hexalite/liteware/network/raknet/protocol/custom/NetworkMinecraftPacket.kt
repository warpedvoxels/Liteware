package me.hexalite.liteware.network.raknet.protocol.custom

import java.net.InetSocketAddress

data class NetworkMinecraftPacket(
    val gamePacket: GamePacket,
    val clientAddress: InetSocketAddress
)
