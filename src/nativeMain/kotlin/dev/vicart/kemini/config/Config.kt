package dev.vicart.kemini.config

import dev.vicart.kemini.exception.ConfigException
import io.ktor.utils.io.*
import io.ktor.utils.io.core.writeText
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.properties.Properties
import kotlinx.serialization.properties.decodeFromStringMap
import kotlinx.serialization.properties.encodeToStringMap

/**
 * The system config path <br>
 * On Linux, it's ~/.config/kemini <br>
 * On Windows, it's %APPDATA%\kemini
 */
expect val SystemConfigPath: String



/**
 * Configuration manager
 */
object Config {

    lateinit var current: ConfigFile

    /**
     * Reads the config from the config file
     */
    fun initConfig() {
        val configDir = Path(SystemConfigPath, "kemini")
        val configFile = Path(configDir, "kemini.conf")
        var generateConfig = false
        if(!SystemFileSystem.exists(configDir)) {
            generateConfig = true
        } else {
            if(!SystemFileSystem.exists(configFile)) {
                generateConfig = true
            }
        }
        if(generateConfig) {
            try {
                SystemFileSystem.createDirectories(configDir)
                writeConfig(configFile, ConfigFile())
            } catch (e: Exception) {
                throw ConfigException("Failed to generate config file: ${e.message}")
            }
        }
        val config = try {
            SystemFileSystem.source(configFile).buffered().use {
                parseConfig(it.readText())
            }
        } catch (e: Exception) {
            throw ConfigException("Failed to read config file: ${e.message}")
        }
        current = config
    }

    @OptIn(ExperimentalSerializationApi::class)
    private fun writeConfig(path: Path, config: ConfigFile) {
        SystemFileSystem.sink(path).buffered().use {
            it.writeText(Properties.encodeToStringMap(config).entries.joinToString("\n") { "${it.key}=${it.value}" })
        }
    }

    /**
     * Parses the config file as a key=value map
     * @param text The config text to parse
     * @return The wrapper ConfigFile object
     * @see ConfigFile
     */
    @OptIn(ExperimentalSerializationApi::class)
    private fun parseConfig(text: String) : ConfigFile {
        return buildMap {
            text.lines().forEach { line ->
                line.split("=").takeIf { it.size == 2 }?.let {
                    put(it[0].trim(), it[1].trim())
                }
            }
        }.let { Properties.decodeFromStringMap(it) }
    }
}