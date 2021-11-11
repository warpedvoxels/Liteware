@file:JvmName("SessionExtensions")
package me.hexalite.liteware.network.session

import me.hexalite.liteware.network.LitewareRakNetServer

inline val LitewareRakNetServer.sessions get() = cache.sessions
