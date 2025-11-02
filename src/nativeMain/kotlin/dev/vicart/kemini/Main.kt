package dev.vicart.kemini

import dev.vicart.kemini.chat.KeminiChat
import dev.vicart.kemini.config.Config

fun main(args: Array<String>) {
    Config.initConfig()

    val chat = KeminiChat()
    chat.startChat()
}