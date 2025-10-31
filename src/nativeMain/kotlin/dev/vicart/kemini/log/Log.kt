package dev.vicart.kemini.log

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toKString
import platform.posix.getenv
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

/**
 * Log utility
 */
@OptIn(ExperimentalTime::class, ExperimentalForeignApi::class)
object Log {

    private const val DEBUG = "DEBUG"

    /**
     * Sends a debug message to the log
     */
    fun debug(message: String) {
        if(getenv("KEMINI_DEBUG")?.toKString()?.let { it != "" } ?: false) {
            log(DEBUG, message)
        }
    }

    private fun log(level: String, message: String) = println("${Clock.System.now()} [$level] $message")
}