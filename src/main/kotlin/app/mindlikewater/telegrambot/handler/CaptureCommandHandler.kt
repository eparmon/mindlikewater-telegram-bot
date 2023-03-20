package app.mindlikewater.telegrambot.handler

import app.mindlikewater.telegrambot.persistence.domain.Chat
import app.mindlikewater.telegrambot.persistence.repository.ChatRepository
import app.mindlikewater.telegrambot.web.client.ApiClient
import app.mindlikewater.telegrambot.web.dto.CaptureRequestDto
import org.springframework.core.ParameterizedTypeReference
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import reactor.core.publisher.Mono

@Component
class CaptureCommandHandler(val apiClient: ApiClient, val chatRepository: ChatRepository) : CommandHandler {

    override fun command(): String {
        return "/capture"
    }

    override fun handle(text: String, chat: Chat): Mono<SendMessage> {
        return if (chat.activeCommand == null) {
            chat.activeCommand = command()
            chatRepository.save(chat)
                .map { SendMessage("${chat.externalId}", "What's on your mind?") }
        } else {
            chat.activeCommand = null
            chatRepository.save(chat)
                .flatMap { sendCaptureRequest(text, chat.id!!) }
                .map { SendMessage("${chat.externalId}", "Now it won't get lost :)") }
                .onErrorReturn(SendMessage("${chat.externalId}", "Something went wrong on our side"))
        }
    }

    private fun sendCaptureRequest(text: String, chatId: Int): Mono<String> {
        return apiClient.post(
            "/capture",
            CaptureRequestDto(text, chatId),
            ParameterizedTypeReference.forType(String::class.java)
        )
    }

}
