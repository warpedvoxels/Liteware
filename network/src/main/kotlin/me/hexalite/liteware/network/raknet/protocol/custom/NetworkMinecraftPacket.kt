package me.hexalite.liteware.network.raknet.protocol.custom

import io.ktor.util.network.*
import me.hexalite.liteware.protocol.packet.MinecraftPacket

data class NetworkMinecraftPacket(
    val rawPacket: EncapsulatedPacket,
    val minecraftPacket: MinecraftPacket,
    val clientAddress: NetworkAddress
)
