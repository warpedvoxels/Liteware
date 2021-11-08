package me.hexalite.liteware.protocol.packet

sealed interface MinecraftPacket {
    abstract class Inbound: MinecraftPacket

    abstract class Outbound: MinecraftPacket
}

typealias InboundPacket = MinecraftPacket.Inbound

typealias OutboundPacket = MinecraftPacket.Outbound
