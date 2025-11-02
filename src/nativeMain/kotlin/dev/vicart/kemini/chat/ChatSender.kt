package dev.vicart.kemini.chat

import kotlinx.serialization.Serializable

/**
 * Message sender <br>
 * USER: the user who typed the message <br>
 * MODEL: the model response
 */
@Serializable
enum class ChatSender {
    USER,
    MODEL
}