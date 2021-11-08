package me.hexalite.liteware.protocol.auth

enum class LoginStatus {
    LoginSuccess,
    FailedClient,
    FailedServer,
    PlayerSpawn,
    FailedInvalidTenant,
    FailedVanillaEducation,
    FailedEducationVanilla,
    FailedServerFull;

    companion object {
        operator fun invoke(ordinal: Int) = values()[ordinal]
    }
}