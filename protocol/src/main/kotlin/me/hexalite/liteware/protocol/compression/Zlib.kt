package me.hexalite.liteware.protocol.compression

import io.ktor.utils.io.core.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.zip.Inflater

class Zlib private constructor(private val inflater: Inflater) {

    companion object Defaults {
        inline fun raw() = Packet
        inline fun default() = World
        val Packet by lazy { Zlib(Inflater(true)) }
        val World by lazy { Zlib(Inflater(false)) }
    }

    private val buffer = ByteArray(2 * 1024 * 1024)

    suspend fun decompress(byteReadPacket: ByteReadPacket) = withContext(Dispatchers.IO) {
        val packet = buildPacket {
            inflater.setInput(byteReadPacket.readBytes())
            inflater.finished()
            var length = 0
            while (!inflater.finished()) {
                val i = inflater.inflate(buffer)
                if (i == 0) {
                    break
                }
                length += i
                writeFully(buffer, 0, i)
            }
        }
        inflater.reset()
        packet
    }

}