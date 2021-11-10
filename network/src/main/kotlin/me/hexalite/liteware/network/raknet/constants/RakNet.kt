package me.hexalite.liteware.network.raknet.constants

object RakNet {
    object Flags {
        const val VALID = 128.toByte()
        const val ACK = 64.toByte()
        const val HAS_B_AND_AS = 32.toByte()
        const val NACK = 32.toByte()
        const val PACKET_PAIR = 16.toByte()
        const val CONTINUOUS_SEND = 8.toByte()
        const val NEEDS_B_AND_AS = 4.toByte()
    }

    // It is needed to be false, otherwise the packet sequence will not work.
    const val USE_SECURITY = false

    // The RakNet protocol version used by Mojang.
    const val RAKNET_PROTOCOL_VERSION: Short = 10

    const val RAKNET_MTU_PADDING = 28
    const val MINIMUM_MTU_SIZE: Short = 576
    const val MAXIMUM_MTU_SIZE: Short = 1400

    const val MAXIMUM_ORDERING_CHANNELS = 16
    const val MAXIMUM_ENCAPSULATED_HEADER_SIZE = 28

    const val UDP_HEADER_SIZE = 8
    const val RAKNET_DATAGRAM_HEADER_SIZE = 4

    const val MAXIMUM_CONNECTION_ATTEMPTS = 10
    const val SESSION_TIMEOUT_MS = 30000
    const val SESSION_STALE_MS = 5000
    const val RAKNET_PING_INTERVAL = 1000
    const val MAXIMUM_STALE_DATAGRAMS = 256

}



