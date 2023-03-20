package app.mindlikewater.telegrambot.web.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebConfig {

    @Value("\${app.api.url}")
    private val apiBaseUrl = ""

    @Bean
    fun webClient(): WebClient {
        return WebClient.create(apiBaseUrl)
    }

}
