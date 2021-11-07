package me.hexalite.liteserver.protocol.datatypes

data class InvalidDataTypeException(override val message: String): Exception(message)
