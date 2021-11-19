package me.hexalite.liteware.network.raknet.protocol.custom

import me.hexalite.liteware.network.annotations.RakNetPacketInfo
import me.hexalite.liteware.network.raknet.protocol.RakNetPacket
import me.hexalite.liteware.network.raknet.protocol.RakNetPacketDetails
import me.hexalite.liteware.protocol.packet.MinecraftPacket

@RakNetPacketInfo(0xfe.toByte())
data class GamePacket(val data: MinecraftPacket, override val details: RakNetPacketDetails) : RakNetPacket
