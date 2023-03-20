package app.mindlikewater.telegrambot.persistence.repository

import app.mindlikewater.telegrambot.persistence.domain.Chat
import org.springframework.data.r2dbc.repository.R2dbcRepository
import reactor.core.publisher.Mono

interface ChatRepository : R2dbcRepository<Chat, Int> {

    fun findByExternalId(externalId: Long) : Mono<Chat>

}
