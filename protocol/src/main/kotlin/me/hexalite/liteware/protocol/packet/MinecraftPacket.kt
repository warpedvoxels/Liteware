package me.hexalite.liteware.protocol.packet

sealed interface MinecraftPacket {
    val id: Int

    abstract class Inbound: MinecraftPacket

    abstract class Outbound: MinecraftPacket
}