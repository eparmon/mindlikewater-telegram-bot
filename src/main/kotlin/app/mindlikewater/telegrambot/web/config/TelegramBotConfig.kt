package app.mindlikewater.telegrambot.web.config

import app.mindlikewater.telegrambot.handler.CommandHandler
import app.mindlikewater.telegrambot.persistence.repository.ChatRepository
import app.mindlikewater.telegrambot.web.bot.Bot
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession


@Configuration
class TelegramBotConfig(val handlers: List<CommandHandler>, val chatRepository: ChatRepository) {

    @Value("\${app.bot.token}")
    private val token = ""

    @Value("\${app.bot.username}")
    private val botUsername = ""

    @Bean
    fun api(): TelegramBotsApi? {
        val telegramBotsApi = TelegramBotsApi(DefaultBotSession::class.java)
        telegramBotsApi.registerBot(Bot(token, botUsername, handlers, chatRepository))
        return telegramBotsApi
    }

}
