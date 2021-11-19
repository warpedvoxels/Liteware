package me.hexalite.liteware.network.raknet.protocol.custom

import io.ktor.utils.io.core.*
import me.hexalite.liteware.network.codec.RakNetPacketCodec
import me.hexalite.liteware.network.raknet.protocol.RakNetPacket
import me.hexalite.liteware.network.raknet.protocol.RakNetPacketDetails

@JvmInline
value class UnknownPacket(override val details: RakNetPacketDetails) : RakNetPacket {

    companion object Codec : RakNetPacketCodec<UnknownPacket>() {

        override suspend fun ByteReadPacket.decode(details: RakNetPacketDetails) = UnknownPacket(details)

    }

}
