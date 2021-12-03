@file:JvmName("PacketUtils")

package me.hexalite.liteware.protocol.utils

import me.hexalite.liteware.protocol.annotations.MinecraftPacketIdentity
import me.hexalite.liteware.protocol.packet.MinecraftPacket
import kotlin.reflect.KClass
import kotlin.reflect.full.findAnnotation

fun <T : MinecraftPacket> findMinecraftPacketId(clazz: KClass<T>): Int = clazz.findAnnotation<MinecraftPacketIdentity>()?.id
    ?: error("Target class is not annotated with @MinecraftPacketInfo.")

inline fun <reified T : MinecraftPacket> findMinecraftPacketId() = findMinecraftPacketId(T::class)

inline fun <T : MinecraftPacket> KClass<T>.id() = findMinecraftPacketId(this)

inline fun MinecraftPacket.id() = findMinecraftPacketId(this::class)
