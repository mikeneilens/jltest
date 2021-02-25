import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.http.*
import io.ktor.utils.io.*
import io.netty.handler.codec.http.HttpResponseStatus

object MockClient
fun createMockClient(expectedUrl:String, expectedMethod: HttpMethod, response:String, responseCode:HttpStatusCode = HttpStatusCode.OK )  =
    HttpClient(MockEngine) {
        engine {
            addHandler { request ->
                if (request.url.toString().removeSuffix("/") == expectedUrl.removeSuffix("/") && request.method == expectedMethod) {
                    val responseHeaders = headersOf("Content-Type" to  listOf(ContentType.Application.Json.toString()))
                    respond(ByteReadChannel(response.toByteArray(Charsets.UTF_8)), status = responseCode,  headers = responseHeaders)
                } else {
                    val responseHeaders = headersOf("Content-Type" to  listOf(ContentType.Application.Json.toString()))
                    respond(response, status = HttpStatusCode.BadRequest,  headers = responseHeaders)
                }
            }
        }
        install(JsonFeature) {
            serializer = JacksonSerializer(jacksonObjectMapper(), ObjectMapper::jacksonConfiguration)
        }
        defaultRequest {
            contentType(ContentType.Application.Json)
        }
        expectSuccess = false
    }