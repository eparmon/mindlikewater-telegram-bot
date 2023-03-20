package app.mindlikewater.telegrambot.web.client

import app.mindlikewater.core.logging.logger
import app.mindlikewater.telegrambot.web.dto.ApiErrorDto
import app.mindlikewater.telegrambot.web.exception.ApiException
import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.client.ClientResponse
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Component
class ApiClient(val webClient: WebClient) {

    val log = logger()

    fun <T : Any> post(uri: String, body: Any, responseType: ParameterizedTypeReference<T>): Mono<T> {
        log.info("Sending POST request to $uri with body $body")
        return webClient.post()
            .uri(uri)
            .bodyValue(body)
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .onStatus({ status -> status.isError }, { buildException(it) })
            .bodyToMono(responseType)
            .doOnNext { response -> log.info("Response: $response") }
    }

    private fun buildException(response: ClientResponse): Mono<ApiException> {
        return response.bodyToMono(ApiErrorDto::class.java)
            .doOnNext { log.warning("API returned error: ${response.statusCode()}, ${it.errorMessage}") }
            .map { ApiException(response.statusCode(), it.errorMessage) }
    }

}
