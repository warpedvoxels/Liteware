package me.hexalite.liteware.network.codec

import io.ktor.utils.io.core.*
import me.hexalite.liteware.network.raknet.protocol.RakNetPacket
import me.hexalite.liteware.network.raknet.protocol.RakNetPacketDetails

abstract class RakNetPacketCodec<T : RakNetPacket> {
    open suspend fun ByteReadPacket.decode(details: RakNetPacketDetails): T {
        error("This packet does not need to be decoded.")
    }

    open suspend fun BytePacketBuilder.encode(packet: T, details: RakNetPacketDetails) {
        error("This packet does not need to be encoded.")
    }
}

suspend inline fun <T : RakNetPacket> RakNetPacketCodec<T>.encode(
    output: BytePacketBuilder,
    packet: T,
    details: RakNetPacketDetails
) = output.encode(packet, details)

suspend inline fun <T : RakNetPacket> RakNetPacketCodec<T>.decode(input: ByteReadPacket, details: RakNetPacketDetails) =
    input.decode(details)