package me.hexalite.liteware.network

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import me.hexalite.liteware.protocol.ProtocolVersion
import java.net.SocketAddress

data class RakNetServerInfo(
    val dispatcher: CoroutineDispatcher = Dispatchers.IO,
    val hostname: String = "127.0.0.1",
    val port: Short = 19132,
    // Get the latest one by default.
    // Java edition wiki reference: https://wiki.vg/Protocol#Protocol_version
    // Bedrock edition protocol reference: https://github.com/CloudburstMC/Protocol#protocol-versions
    val supportedProtocolVersions: List<ProtocolVersion> = listOf(ProtocolVersion.values().last()),
    val blockedAddresses: List<SocketAddress> = emptyList()
)
