package me.hexalite.liteware.protocol.packet

import me.hexalite.liteware.protocol.binary.MinecraftPacketCodec

object EmptyMinecraftPacket: MinecraftPacket {
    override val id: Int
        get() = -1

    override val codec: MinecraftPacketCodec
        get() = error("Packet is empty.")
}