package app.mindlikewater.telegrambot.handler

import app.mindlikewater.telegrambot.persistence.domain.Chat
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import reactor.core.publisher.Mono

interface CommandHandler {

    fun command(): String

    fun handle(text: String, chat: Chat): Mono<SendMessage>

}
