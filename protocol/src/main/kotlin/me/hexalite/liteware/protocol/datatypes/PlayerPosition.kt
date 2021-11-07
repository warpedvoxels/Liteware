package me.hexalite.liteware.protocol.datatypes

import io.ktor.utils.io.core.*

data class PlayerPosition(val vector: Vector3D, val pitch: Float, val headYaw: Float, val yaw: Float) {

    inline val x get() = vector.x

    inline val y get() = vector.y

    inline val z get() = vector.z

}

private fun BytePacketBuilder.writeVector3D(vector: Vector3D) {
    writeFloat(vector.x)
    writeFloat(vector.y)
    writeFloat(vector.z)
}

private fun ByteReadPacket.readVector3D() = Vector3D(readFloat(), readFloat(), readFloat())

fun BytePacketBuilder.writePlayerPosition(playerPosition: PlayerPosition) {
    writeVector3D(playerPosition.vector)
    writeFloat(playerPosition.pitch)
    writeFloat(playerPosition.headYaw)
    writeFloat(playerPosition.yaw)
}

fun ByteReadPacket.readPlayerPosition() = PlayerPosition(readVector3D(), readFloat(), readFloat(), readFloat())
