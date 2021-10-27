package me.hexalite.software.protocol.datatypes

data class InvalidDataTypeException(override val message: String): Exception(message)
