import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.features.json.*
import io.ktor.http.*

object MockClient
fun createMockClient(expectedUrl:String, expectedMethod: HttpMethod, response:String)  =
    HttpClient(MockEngine) {
        engine {
            addHandler { request ->
                if (request.url.toString().removeSuffix("/") == expectedUrl.removeSuffix("/") && request.method == expectedMethod) {
                    val responseHeaders = headersOf("Content-Type" to listOf(ContentType.Application.Json.toString()))
                    respond(response, headers = responseHeaders)
                } else {
                    respond("Unexpected reqeuest. Expected ${request.method} ${request.url}. Expected $expectedMethod $expectedUrl", HttpStatusCode.BadRequest)
                }
            }
        }
        install(JsonFeature) {
            serializer = JacksonSerializer(jacksonObjectMapper(), ObjectMapper::jacksonConfiguration)
        }
    }