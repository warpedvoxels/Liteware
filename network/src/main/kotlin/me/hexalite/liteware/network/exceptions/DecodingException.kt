package me.hexalite.liteware.network.exceptions

data class DecodingException(override val message: String) : Exception(message)
