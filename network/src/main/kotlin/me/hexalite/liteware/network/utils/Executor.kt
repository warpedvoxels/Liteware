package me.hexalite.liteware.network.utils

fun interface Executor<T> {
    suspend fun middleware(event: T)
}