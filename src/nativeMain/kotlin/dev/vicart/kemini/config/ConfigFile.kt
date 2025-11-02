package dev.vicart.kemini.config

import kotlinx.serialization.Serializable

/**
 * Configuration wrapper of the config file
 * @param model The model name to use
 * @param apiKey The API key to use
 * @param printDelay The delay between each sentence printed in the chat by the model
 * @param maxOutputTokens The maximum number of tokens to generate by the model
 */
@Serializable
data class ConfigFile(
    val model: String = "gemini-2.5-flash",
    val apiKey: String = "",
    val printDelay: Long = 0L,
    val maxOutputTokens: Int = 3000
)