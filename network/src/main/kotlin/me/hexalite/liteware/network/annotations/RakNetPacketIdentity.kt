package me.hexalite.liteware.network.annotations

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class RakNetPacketIdentity(val id: Byte)
