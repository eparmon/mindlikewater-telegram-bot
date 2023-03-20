package app.mindlikewater.telegrambot.handler

import app.mindlikewater.telegrambot.persistence.domain.Chat
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import reactor.core.publisher.Mono

@Component
class StartCommandHandler : CommandHandler  {

    override fun command(): String {
        return "/start"
    }

    override fun handle(text: String, chat: Chat): Mono<SendMessage> {
        return Mono.just(SendMessage("${chat.externalId}", "Welcome!"))
    }

}
