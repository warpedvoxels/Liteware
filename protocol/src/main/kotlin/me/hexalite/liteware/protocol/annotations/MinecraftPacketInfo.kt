package me.hexalite.liteware.protocol.annotations

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.SOURCE)
annotation class MinecraftPacketInfo(val id: Int)
