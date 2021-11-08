package me.hexalite.liteware.network.session

import me.hexalite.liteware.protocol.packet.MinecraftPacket

data class NetworkPacket(val packet: MinecraftPacket, val session: NetworkPlayerSession)
