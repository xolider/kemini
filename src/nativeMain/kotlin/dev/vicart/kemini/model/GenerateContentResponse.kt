package dev.vicart.kemini.model

import dev.vicart.kemini.chat.ChatSender
import kotlinx.serialization.Serializable

@Serializable
data class GenerateContentResponse(
    val candidates: List<Candidate>
) {
    fun getFullText() = candidates.flatMap { it.content.parts }.map { it.text }
}
