import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.features.json.*
import io.ktor.http.*

object MockClient
fun createMockClient(expectedUrl:String, expectedMethod: HttpMethod, response:String, responseCode:HttpStatusCode = HttpStatusCode.OK )  =
    HttpClient(MockEngine) {
        engine {
            addHandler { request ->
                val responseHeaders = headersOf("Content-Type" to  listOf(ContentType.Application.Json.toString()))
                if (request.url.toString().removeSuffix("/") == expectedUrl.removeSuffix("/") && request.method == expectedMethod) {
                    respond(response, status = responseCode,  headers = responseHeaders)
                } else {
                    respond(response, status = HttpStatusCode.BadRequest,  headers = responseHeaders)
                }
            }
        }
        install(JsonFeature) {
            serializer = JacksonSerializer(jacksonObjectMapper(), ObjectMapper::jacksonConfiguration)
        }
    }