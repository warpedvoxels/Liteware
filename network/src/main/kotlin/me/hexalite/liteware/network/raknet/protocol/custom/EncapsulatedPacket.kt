package me.hexalite.liteware.network.raknet.protocol.custom

import io.ktor.utils.io.core.*
import me.hexalite.liteware.network.codec.RakNetPacketCodec
import me.hexalite.liteware.network.datatypes.RakNetPriority
import me.hexalite.liteware.network.datatypes.RakNetReliability
import me.hexalite.liteware.network.datatypes.readUInt24LE
import me.hexalite.liteware.network.datatypes.writeUInt24LE
import me.hexalite.liteware.network.raknet.protocol.RakNetPacket
import me.hexalite.liteware.network.raknet.protocol.RakNetPacketDetails

@OptIn(ExperimentalUnsignedTypes::class, ExperimentalIoApi::class)
data class EncapsulatedPacket(
    val reliability: RakNetReliability,
    val priority: RakNetPriority,
    val reliabilityIndex: Int,
    val sequenceIndex: Int,
    val orderingIndex: Int,
    val orderingChannel: Short,
    val split: Boolean,
    val partCount: Int,
    val partId: Int,
    val partIndex: Int,
    val data: ByteReadPacket,
    override val details: RakNetPacketDetails
) : RakNetPacket {

    companion object Codec : RakNetPacketCodec<EncapsulatedPacket>() {

        override fun ByteReadPacket.decode(details: RakNetPacketDetails): EncapsulatedPacket {
            val flags = readByte().toInt()
            val reliability = RakNetReliability[(flags and 224) shr 5]
            val split = (flags and 16) != 0
            val reliabilityIndex = if (reliability.reliable) readUInt24LE() else 0
            val sequenceIndex = if (reliability.sequenced) readUInt24LE() else 0
            val isSequencedOrOrdered = reliability.ordered || reliability.sequenced
            val orderingIndex = if (isSequencedOrOrdered) readUInt24LE() else 0
            val orderingChannel = if (isSequencedOrOrdered) readUByte().toShort() else 0
            val partCount = if (split) readInt() else 0
            val partId = if (split) readUShort().toInt() else 0
            val partIndex = if (split) readInt() else 0
            return EncapsulatedPacket(
                reliability = reliability,
                priority = RakNetPriority[(flags and 3)],
                reliabilityIndex = reliabilityIndex,
                sequenceIndex = sequenceIndex,
                orderingIndex = orderingIndex,
                orderingChannel = orderingChannel,
                split = split,
                partCount = partCount,
                partId = partId,
                partIndex = partIndex,
                data = ByteReadPacket(readBytes()),
                details
            )
        }

        override fun BytePacketBuilder.encode(packet: EncapsulatedPacket, details: RakNetPacketDetails) {
            val flags = ((packet.reliabilityIndex shl 5) or (if (packet.split) 16 else 0)).toByte()
            writeByte(flags)
            writeShort((packet.data.remaining shl 3).toShort())
            if (packet.reliability.reliable) writeUInt24LE(packet.reliabilityIndex)
            if (packet.reliability.sequenced) writeUInt24LE(packet.sequenceIndex)
            if (packet.reliability.ordered || packet.reliability.sequenced) {
                writeUInt24LE(packet.orderingIndex)
                writeUByte(packet.orderingChannel.toUByte())
            }
            if (packet.split) {
                writeInt(packet.partCount)
                writeUShort(packet.partId.toUShort())
                writeInt(packet.partIndex)
            }
            writePacket(packet.data)
        }

    }

}