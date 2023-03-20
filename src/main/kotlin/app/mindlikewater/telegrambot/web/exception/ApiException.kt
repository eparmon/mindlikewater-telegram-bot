package app.mindlikewater.telegrambot.web.exception

import org.springframework.http.HttpStatusCode

class ApiException(val status: HttpStatusCode, message: String?) : Exception(message)
