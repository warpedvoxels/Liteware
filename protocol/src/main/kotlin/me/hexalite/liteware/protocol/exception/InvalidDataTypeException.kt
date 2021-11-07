package me.hexalite.liteware.protocol.exception

data class InvalidDataTypeException(override val message: String): Exception(message)
