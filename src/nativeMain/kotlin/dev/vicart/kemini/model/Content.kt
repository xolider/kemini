package dev.vicart.kemini.model

import kotlinx.serialization.Serializable

@Serializable
data class Content(
    val parts: List<Part>,
    val role: String? = null
)
