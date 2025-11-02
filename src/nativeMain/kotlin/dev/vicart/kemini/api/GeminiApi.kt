package dev.vicart.kemini.api

import dev.vicart.kemini.chat.ChatSender
import dev.vicart.kemini.config.Config
import dev.vicart.kemini.model.GenerateContentRequest
import dev.vicart.kemini.model.GenerateContentResponse
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.sse.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.utils.io.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.filterNot
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

private val geminiApiUrl: (String, String?) -> String = { model, apiKey ->
    "https://generativelanguage.googleapis.com/v1beta/models/$model:streamGenerateContent?alt=sse&key=$apiKey"
}

/**
 * Gemini API client
 */
class GeminiApi {

    private val json = Json { ignoreUnknownKeys = true }

    private val client = HttpClient {
        install(SSE)
        install(ContentNegotiation) {
            json(json = json)
        }
    }

    /**
     * Sends a prompt to the Gemini model
     * @param history The history of the current chat
     */
    @OptIn(InternalAPI::class)
    fun sendPrompt(history: List<Pair<ChatSender, String>>) = channelFlow {
        withContext(Dispatchers.IO) {
            client.sse(urlString = geminiApiUrl(Config.current.model, Config.current.apiKey), request = {
                contentType(ContentType.Application.Json)
                setBody(GenerateContentRequest.fromHistory(history))
                method = HttpMethod.Post
            }) {
                incoming.filterNot { it.data == null }.map { json.decodeFromString<GenerateContentResponse>(it.data!!) }
                    .collect {
                        send(it)
                    }
            }
        }
    }
}