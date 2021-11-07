package me.hexalite.liteware.protocol.datatypes

import io.ktor.utils.io.core.*

sealed class Vector<T: Number> {
    abstract val x: T

    abstract val y: T

    data class `3D`(override val x: Float, override val y: Float, val z: Float): Vector<Float>() {
        data class Integer(override val x: Int, override val y: Int, val z: Int): Vector<Int>()
    }

    data class `2D`(override val x: Float, override val y: Float): Vector<Float>() {
        data class Integer(override val x: Int, override val y: Int): Vector<Int>()
    }
}

typealias Vector3D = Vector.`3D`
typealias Vector2D = Vector.`2D`
typealias Vector3DInt = Vector.`3D`.Integer
typealias Vector2DInt = Vector.`2D`.Integer

fun BytePacketBuilder.writeBlockCoordinates(vector: Vector3DInt) {
    writeVarInt(VarInt(vector.x))
    writeVarInt(VarInt(vector.y))
    writeVarInt(VarInt(vector.z))
}

fun ByteReadPacket.readBlockCoordinates(): Vector3DInt {
    val x = readVarInt().toInt()
    val y = readVarInt().toInt()
    val z = readVarInt().toInt()
    return Vector3DInt(x, y, z)
}
