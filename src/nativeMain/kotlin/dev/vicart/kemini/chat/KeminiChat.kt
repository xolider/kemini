package dev.vicart.kemini.chat

import dev.vicart.kemini.api.GeminiApi
import dev.vicart.kemini.config.Config
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

/**
 * Message sender
 * USER: the user who typed the message
 * MODEL: the model response
 */
private enum class Sender {
    USER,
    MODEL
}

/**
 * Core logic of the chat
 */
class KeminiChat {

    private var running = true

    private val api = GeminiApi()

    private val history = mutableMapOf<Sender, String>()

    private val promptScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    /**
     * Starts the chat loop
     */
    fun startChat() {
        println("Gemini model: ${Config.current.model}")
        println()
        var currentJob: Job? = null
        while (running) {
            print("You > ")
            val input = readlnOrNull() ?: break
            if(input.startsWith('/')) {
                parseCommand(input.substring(1))
            } else {
                currentJob?.cancel()
                currentJob = promptScope.launch {
                    api.sendPrompt(input)
                }
            }
        }
        promptScope.cancel()
    }

    /**
     * Parses a command sent in the chat
     * @param command The command to parse
     */
    private fun parseCommand(command: String): Unit = when(command) {
        "quit" -> running = false
        else -> println("Unknown command: $command. Type /help for help")
    }
}