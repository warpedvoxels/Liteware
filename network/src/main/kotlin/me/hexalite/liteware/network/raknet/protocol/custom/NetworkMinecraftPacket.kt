package me.hexalite.liteware.network.raknet.protocol.custom

import me.hexalite.liteware.network.session.NetworkPlayerSession
import me.hexalite.liteware.protocol.packet.MinecraftPacket

data class NetworkMinecraftPacket(
    val rawPacket: EncapsulatedPacket,
    val minecraftPacket: MinecraftPacket,
    val session: NetworkPlayerSession
)
