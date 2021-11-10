package me.hexalite.liteware.network.raknet.protocol.custom

import io.ktor.utils.io.core.*
import me.hexalite.liteware.network.codec.RakNetPacketCodec
import me.hexalite.liteware.network.codec.decode
import me.hexalite.liteware.network.codec.encode
import me.hexalite.liteware.network.datatypes.readUInt24LE
import me.hexalite.liteware.network.datatypes.writeUInt24LE
import me.hexalite.liteware.network.raknet.constants.RakNet
import me.hexalite.liteware.network.raknet.protocol.RakNetPacket
import me.hexalite.liteware.network.raknet.protocol.RakNetPacketDetails

@OptIn(ExperimentalStdlibApi::class, ExperimentalIoApi::class)
data class RakNetCustomPacket(
    val sequenceIndex: Int,
    val messages: List<EncapsulatedPacket>,
    val flags: Byte = RakNet.Flags.VALID,
    override val details: RakNetPacketDetails
) : RakNetPacket {

    companion object Codec : RakNetPacketCodec<RakNetCustomPacket>() {

        override fun ByteReadPacket.decode(details: RakNetPacketDetails): RakNetCustomPacket {
            val flags = readByte()
            val sequenceIndex = readUInt24LE()
            val messages = buildList {
                while (canRead()) {
                    val packet = EncapsulatedPacket.Codec.decode(this@decode, details)
                    add(packet)
                }
            }
            return RakNetCustomPacket(sequenceIndex, messages, flags, details)
        }

        override fun BytePacketBuilder.encode(packet: RakNetCustomPacket, details: RakNetPacketDetails) {
            writeByte(packet.flags)
            writeUInt24LE(packet.sequenceIndex)
            packet.messages.forEach { EncapsulatedPacket.Codec.encode(this, it, details) }
        }

    }

}
