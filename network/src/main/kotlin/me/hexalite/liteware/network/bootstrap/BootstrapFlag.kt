package me.hexalite.liteware.network.bootstrap

sealed class BootstrapFlag(val name: String, val shortName: String, val description: String) {

    object Windows : BootstrapFlag(
        "Windows",
        "w",
        "Enable Windows support by enabling communication with the Minecraft UWP app."
    )

}
