package me.hexalite.liteware.network.session

import dev.kord.cache.api.DataCache
import dev.kord.cache.api.put
import dev.kord.cache.api.query
import dev.kord.cache.api.remove
import io.ktor.util.network.*

class Sessions(private val cache: DataCache) {
    fun asFlow() = cache.query<NetworkPlayerSession>().asFlow()

    suspend fun find(address: NetworkAddress) = cache.query<NetworkPlayerSession> {
        NetworkPlayerSession::address eq address
    }.singleOrNull()

    suspend fun delete(address: NetworkAddress) = cache.remove<NetworkPlayerSession> {
        NetworkPlayerSession::address eq address
    }

    suspend fun create(networkPlayerSession: NetworkPlayerSession) = cache.put(networkPlayerSession)
}
