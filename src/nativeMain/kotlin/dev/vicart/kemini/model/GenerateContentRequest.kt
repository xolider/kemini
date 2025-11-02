package dev.vicart.kemini.model

import dev.vicart.kemini.chat.ChatSender
import dev.vicart.kemini.config.Config
import kotlinx.serialization.Serializable

@Serializable
data class GenerateContentRequest(
    val contents: List<Content>,
    val generationConfig: GenerationConfig? = null
) {
    companion object {
        fun fromHistory(history: List<Pair<ChatSender, String>>) : GenerateContentRequest {
            val contents = history.map {
                Content(parts = listOf(Part(it.second)), role = it.first.name.lowercase())
            }
            return GenerateContentRequest(
                contents = contents,
                generationConfig = GenerationConfig(Config.current.maxOutputTokens)
            )
        }
    }
}
