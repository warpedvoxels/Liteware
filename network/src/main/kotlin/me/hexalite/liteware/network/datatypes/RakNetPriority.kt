package me.hexalite.liteware.network.datatypes

enum class RakNetPriority {
    IMMEDIATE, HIGH, MEDIUM, LOW;

    companion object {
        operator fun get(index: Int) = values().getOrNull(index) ?: LOW
    }
}