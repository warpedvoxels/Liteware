package me.hexalite.liteware.protocol.annotations

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class MinecraftPacketIdentity(val id: Int)
