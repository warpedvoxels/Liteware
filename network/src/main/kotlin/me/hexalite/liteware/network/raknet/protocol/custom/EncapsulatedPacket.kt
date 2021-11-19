package me.hexalite.liteware.network.raknet.protocol.custom

import io.ktor.utils.io.core.*
import me.hexalite.liteware.network.codec.RakNetPacketCodec
import me.hexalite.liteware.network.datatypes.RakNetReliability
import me.hexalite.liteware.network.datatypes.readUInt24LE
import me.hexalite.liteware.network.datatypes.writeUInt24LE
import me.hexalite.liteware.network.exceptions.DecodingException
import me.hexalite.liteware.network.raknet.protocol.RakNetPacket
import me.hexalite.liteware.network.raknet.protocol.RakNetPacketDetails
import me.hexalite.liteware.network.session.adjustedMtu
import me.hexalite.liteware.network.session.sessions

@OptIn(ExperimentalUnsignedTypes::class, ExperimentalIoApi::class)
data class EncapsulatedPacket(
    val reliability: RakNetReliability,
    val reliabilityIndex: Int,
    val sequenceIndex: Int,
    val ordering: Ordering?,
    val split: Split?,
    val data: ByteReadPacket,
    override val details: RakNetPacketDetails
) : RakNetPacket {

    data class Ordering(val index: Int, val channel: Int)

    data class Split(val count: Int, val id: Int, val index: Int)

    companion object Codec : RakNetPacketCodec<EncapsulatedPacket>() {

        override suspend fun ByteReadPacket.decode(details: RakNetPacketDetails): EncapsulatedPacket {
            if (remaining < 4) {
                throw DecodingException("Insufficient data to decode; minimum is 4 bytes.")
            }
            val flags = readUByte().toInt()
            val length: Int = (readUShort().toInt() + 7) / 8

            val mtu = details.server.sessions.find(details.clientAddress)?.mtu
            when {
                mtu == null -> throw DecodingException("A custom packet shouldn't be decoded within a nonexistent session.")
                length > mtu -> throw DecodingException("The packet length should not be greater than MTU.")
                length <= 0 -> throw DecodingException("The packet length must be greater than 0.")
            }

            val reliability = RakNetReliability[flags shr 5]
            val reliabilityIndex = if (reliability.isReliable) readUInt24LE() else 0
            val sequenceIndex = if (reliability.isSequenced) readUInt24LE() else 0

            val ordering = if (reliability.isOrdered) Ordering(readUInt24LE(), readUByte().toInt()) else null
            val split = if ((flags and 0x10) != 0) Split(readInt(), readUShort().toInt(), readInt()) else null

            if (remaining < length) {
                throw DecodingException("EncapsulatedPacket: Not enough data to decode packet. [$length/$remaining]")
            }

            val packet = ByteReadPacket(readBytes(length))
            return EncapsulatedPacket(reliability, reliabilityIndex, sequenceIndex, ordering, split, packet, details)
        }

        override suspend fun BytePacketBuilder.encode(packet: EncapsulatedPacket, details: RakNetPacketDetails) {
            writeByte((packet.reliability.ordinal shl 5 or if (packet.split != null) 0x10 else 0).toByte())
            writeShort((packet.data.remaining shl 3).toShort())
            if (packet.reliability.isReliable) {
                writeUInt24LE(packet.reliability.ordinal)
            }
            if (packet.reliability.isSequenced) {
                writeUInt24LE(packet.sequenceIndex)
            }
            if (packet.reliability.isOrdered) {
                writeUInt24LE(packet.ordering!!.index)
                writeByte(packet.ordering.channel.toByte())
            }
            if (packet.split != null) {
                writeInt(packet.split.count)
                writeShort(packet.split.id.toShort())
                writeInt(packet.split.index)
            }
            writePacket(packet.data)
        }

        private var nextSplitId: Int = 0

        suspend fun EncapsulatedPacket.split(details: RakNetPacketDetails): List<EncapsulatedPacket> {
            val maximumSize = (details.server.sessions.find(details.clientAddress)?.adjustedMtu?.toInt()
                ?: error("A custom packet shouldn't be split within a nonexistent session."))

            val length: Int = data.remaining.toInt()
            val splitPacketCount = (length - 1) / maximumSize + 1
            val splitPacketId = if (++nextSplitId == 1 shl 16) 0 else nextSplitId
            val allBytes = data.readBytes()
            var cursor = 0
            var count: Int

            return buildList {
                for (splitPacketIndex in 0 until splitPacketCount) {
                    count = length - cursor
                    if (count > maximumSize) {
                        count = maximumSize
                    }
                    val copy = copy(
                        data = ByteReadPacket(allBytes.copyOfRange(cursor, count)),
                        split = Split(splitPacketId, splitPacketCount, splitPacketIndex)
                    )
                    add(copy)
                    cursor += count
                }
            }
        }

    }

}

suspend fun splitPacketFrom(packet: EncapsulatedPacket, details: RakNetPacketDetails) =
    with(EncapsulatedPacket.Codec) { packet.split(details) }

