package me.hexalite.liteware.network.raknet

import io.ktor.utils.io.core.*

sealed interface RakNet {
    object Flags {
        const val VALID = 128.toByte()
        const val ACK = 64.toByte()
        const val HAS_B_AND_AS = 32.toByte()
        const val NACK = 32.toByte()
        const val PACKET_PAIR = 16.toByte()
        const val CONTINUOUS_SEND = 8.toByte()
        const val NEEDS_B_AND_AS = 4.toByte()
    }

    object Packets {
        const val CONNECTED_PING: Byte = 0x00
        const val UNCONNECTED_PING: Byte = 0x01
        const val UNCONNECTED_PING_OPEN_CONNECTIONS: Byte = 0x02
        const val CONNECTED_PONG: Byte = 0x03
        const val DETECT_LOST_CONNECTION: Byte = 0x04
        const val OPEN_CONNECTION_REQUEST_1: Byte = 0x05
        const val OPEN_CONNECTION_REPLY_1: Byte = 0x06
        const val OPEN_CONNECTION_REQUEST_2: Byte = 0x07
        const val OPEN_CONNECTION_REPLY_2: Byte = 0x08
        const val CONNECTION_REQUEST: Byte = 0x09
        const val CONNECTION_REQUEST_ACCEPTED: Byte = 0x10
        const val CONNECTION_REQUEST_FAILED: Byte = 0x11
        const val ALREADY_CONNECTED: Byte = 0x12
        const val NEW_INCOMING_CONNECTION: Byte = 0x13
        const val NO_FREE_INCOMING_CONNECTIONS: Byte = 0x14
        const val DISCONNECTION_NOTIFICATION: Byte = 0x15
        const val CONNECTION_LOST: Byte = 0x16
        const val CONNECTION_BANNED: Byte = 0x17
        const val INCOMPATIBLE_PROTOCOL_VERSION: Byte = 0x19
        const val IP_RECENTLY_CONNECTED: Byte = 0x1a
        const val TIMESTAMP: Byte = 0x1b
        const val UNCONNECTED_PONG: Byte = 0x1c
        const val ADVERTISE_SYSTEM: Byte = 0x1d
        const val USER_PACKET_ENUM: Byte = 0x80.toByte()
    }

    companion object {
        const val RAKNET_PROTOCOL_VERSION: Short = 10

        const val MINIMUM_MTU_SIZE: Short = 576
        var MAXIMUM_MTU_SIZE: Short = 1400

        const val MAXIMUM_ORDERING_CHANNELS = 16
        const val MAXIMUM_ENCAPSULATED_HEADER_SIZE = 28

        const val UDP_HEADER_SIZE = 8
        const val RAKNET_DATAGRAM_HEADER_SIZE = 4

        const val MAXIMUM_CONNECTION_ATTEMPTS = 10
        const val SESSION_TIMEOUT_MS = 30000
        const val SESSION_STALE_MS = 5000
        const val RAKNET_PING_INTERVAL = 1000
        const val MAXIMUM_STALE_DATAGRAMS = 256

        val UNCONNECTED_MAGIC = byteArrayOf(0, -1, -1, 0, -2, -2, -2, -2, -3, -3, -3, -3, 18, 52, 86, 120)

        fun isUnconnectedMagic(data: ByteReadPacket): Boolean {
            val magic = data.readBytes(UNCONNECTED_MAGIC.size)
            return magic.contentEquals(UNCONNECTED_MAGIC)
        }
    }
}