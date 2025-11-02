package dev.vicart.kemini.model

import kotlinx.serialization.Serializable

@Serializable
data class Candidate(
    val content: Content,
    val index: Int
)
