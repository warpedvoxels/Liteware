package me.hexalite.liteware.protocol.server

data class ServerProperties(
    var motd: String,
    var versionName: String = "1.17",
    var maxPlayers: Int
)
