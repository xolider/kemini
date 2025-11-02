package dev.vicart.kemini.model

import kotlinx.serialization.Serializable

@Serializable
data class GenerationConfig(
    val maxOutputTokens: Int? = null
)
