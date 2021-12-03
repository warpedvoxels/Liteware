package me.hexalite.liteware.network.raknet.protocol.custom

import me.hexalite.liteware.network.annotations.RakNetPacketIdentity
import me.hexalite.liteware.network.raknet.protocol.RakNetPacket
import me.hexalite.liteware.network.raknet.protocol.RakNetPacketDetails
import me.hexalite.liteware.protocol.packet.MinecraftPacket

@RakNetPacketIdentity(0xfe.toByte())
data class GamePacket(val data: MinecraftPacket, override val details: RakNetPacketDetails) : RakNetPacket
