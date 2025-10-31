package dev.vicart.kemini.config

import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.pointed
import kotlinx.cinterop.toKString
import platform.posix.getenv
import platform.posix.getpwuid
import platform.posix.getuid

@OptIn(ExperimentalForeignApi::class)
actual val SystemConfigPath: String = getenv("XDG_CONFIG_HOME")?.toKString() ?:
    "${getpwuid(getuid())?.pointed?.pw_dir?.toKString() ?: throw Exception("Could not get user home")}/.config"