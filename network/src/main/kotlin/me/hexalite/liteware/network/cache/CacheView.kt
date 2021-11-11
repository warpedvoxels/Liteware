package me.hexalite.liteware.network.cache

import dev.kord.cache.api.DataCache
import dev.kord.cache.api.data.description
import dev.kord.cache.map.MapDataCache
import me.hexalite.liteware.network.raknet.protocol.custom.NetworkMinecraftPacket
import me.hexalite.liteware.network.session.NetworkPlayerSession
import me.hexalite.liteware.network.session.Sessions

suspend fun buildServerDefaultCache(): DataCache {
    val sessionDescription = description(NetworkPlayerSession::address) {
        link(NetworkPlayerSession::address to NetworkMinecraftPacket::clientAddress)
    }
    return MapDataCache().apply {
        register(sessionDescription)
    }
}

data class CacheView(private val internal: DataCache) {

    val sessions = Sessions(internal)


}
