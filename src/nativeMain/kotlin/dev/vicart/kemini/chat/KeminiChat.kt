package dev.vicart.kemini.chat

import dev.vicart.kemini.api.GeminiApi
import dev.vicart.kemini.config.Config
import dev.vicart.kemini.model.GenerateContentResponse
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json

/**
 * Core logic of the chat
 */
class KeminiChat {

    private var running = true

    private val api = GeminiApi()

    private val history = mutableListOf<Pair<ChatSender, String>>()

    /**
     * Starts the chat loop
     */
    fun startChat() {
        println("Gemini model: ${Config.current.model}")
        println()
        while (running) {
            print("You > ")
            val input = readlnOrNull() ?: break
            if(input.startsWith('/')) {
                parseCommand(input.substring(1))
            } else {
                runBlocking {
                    history.add(ChatSender.USER to input)
                    val source = api.sendPrompt(history)
                    handleModelResponse(source)
                }
            }
        }
    }

    /**
     * Handles the response from the model
     * @param response The response flow from the model
     */
    private suspend fun handleModelResponse(response: Flow<GenerateContentResponse>) {
        print("Model > Thinking...")
        val sb = StringBuilder()
        response.onStart {
            print("\rModel > ")
        }.collect {
            it.getFullText().forEach {
                print(it)
                sb.append(it)
            }
            delay(Config.current.printDelay)
        }
        println()
        history.add(ChatSender.MODEL to sb.toString())
    }

    /**
     * Parses a command sent in the chat
     * @param command The command to parse
     */
    private fun parseCommand(command: String): Unit = when(command) {
        "quit" -> running = false
        "history" -> println(Json.encodeToString(history))
        "help" -> println("""
            Available commands:
                /quit: Quit the chat
                /history: Print the chat history
                /help: Print this message
        """.trimIndent())
        else -> println("Unknown command: $command. Type /help for help")
    }
}