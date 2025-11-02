package dev.vicart.kemini.log

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.toKString
import platform.posix.fprintf
import platform.posix.getenv
import platform.posix.stderr
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

/**
 * Log utility
 */
@OptIn(ExperimentalTime::class, ExperimentalForeignApi::class)
object Log {

    private const val DEBUG = "DEBUG"

    val debugEnabled: Boolean = getenv("KEMINI_DEBUG")?.toKString()?.let { it != "" } ?: false

    /**
     * Sends a debug message to the log
     */
    fun debug(message: String) {
        if(debugEnabled) {
            log(DEBUG, message)
        }
    }

    fun error(message: String) = fprintf(stderr, message)

    private fun log(level: String, message: String) = println("${Clock.System.now()} [$level] $message")
}