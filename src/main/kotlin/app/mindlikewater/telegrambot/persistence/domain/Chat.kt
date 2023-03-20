package app.mindlikewater.telegrambot.persistence.domain

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("chats")
class Chat(val externalId: Long) {

    @Id
    var id: Int? = null

    var activeCommand: String? = null

}
