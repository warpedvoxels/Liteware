package me.hexalite.liteware.protocol.datatypes

sealed class Vector {
    data class `3D`(val x: Double, val y: Double, val z: Double): Vector()
    data class `2D`(val x: Double, val y: Double): Vector()
}