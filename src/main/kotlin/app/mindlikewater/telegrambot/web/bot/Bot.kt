package app.mindlikewater.telegrambot.web.bot

import app.mindlikewater.core.logging.logger
import app.mindlikewater.telegrambot.handler.CommandHandler
import app.mindlikewater.telegrambot.persistence.domain.Chat
import app.mindlikewater.telegrambot.persistence.repository.ChatRepository
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import reactor.core.publisher.Mono

class Bot(
    token: String,
    private val botUsername: String,
    private val handlers: List<CommandHandler>,
    private val chatRepository: ChatRepository
) : TelegramLongPollingBot(token) {

    val log = logger()

    override fun getBotUsername(): String {
        return botUsername
    }

    override fun onUpdateReceived(update: Update) {
        val externalChatId = update.message.chatId
        val text = update.message.text
        log.info("Received message from chat $externalChatId: $text")
        chatRepository.findByExternalId(externalChatId)
            .switchIfEmpty(chatRepository.save(Chat(externalChatId)))
            .flatMap { chat -> handle(text, chat) }
            .doOnNext { execute(it) }
            .subscribe()
    }

    private fun handle(text: String, chat: Chat): Mono<SendMessage> {
        val handler = findHandler(chat.activeCommand ?: text)
        return handler?.handle(text, chat) ?: buildErrorMessage(chat.externalId)
    }

    private fun findHandler(command: String): CommandHandler? {
        return handlers.find { handler -> handler.command() == command }
    }

    private fun buildErrorMessage(externalChatId: Long?): Mono<SendMessage> {
        return Mono.just(SendMessage("$externalChatId", "Instructions unclear"))
    }

}
