import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.exc.ValueInstantiationException
import com.typesafe.config.ConfigFactory
import io.ktor.application.*
import io.ktor.client.*
import io.ktor.config.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.jackson.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.netty.handler.codec.http.HttpObjectDecoder
import io.netty.handler.codec.http.HttpServerCodec
import java.net.SocketTimeoutException

const val ApiPortKey = "ktor.deployment.port"

fun main() {
    val env = applicationEngineEnvironment {

        config = HoconApplicationConfig(ConfigFactory.load())

        connector {
            port = config.property(ApiPortKey).getString().toInt()
        }
    }

    embeddedServer(Netty, env, configure = {
        httpServerCodec = {
            HttpServerCodec(HttpObjectDecoder.DEFAULT_MAX_INITIAL_LINE_LENGTH, HttpObjectDecoder.DEFAULT_MAX_HEADER_SIZE * 4, HttpObjectDecoder.DEFAULT_MAX_CHUNK_SIZE)
        }
    }).start(true)
}

@Suppress("unused") // Referenced in application.conf
fun Application.mainModule(
    client: HttpClient = client()
) {

    install(DefaultHeaders)
    install(CallLogging)

    install(ContentNegotiation) {
        jackson {
            jacksonConfiguration()
        }
    }

    install(StatusPages) {
        exception<BadRequestException> { cause ->
            call.respond(HttpStatusCode.BadRequest, cause.error)
        }

        exception<ValueInstantiationException> { cause ->
            when (val realCause = cause.cause) {
                is BadRequestException -> call.respond(HttpStatusCode.BadRequest, realCause.error)
                else -> call.respond(HttpStatusCode.BadRequest, ErrorResponse("invalid.json"))
            }
        }

        exception<JsonProcessingException> {
            call.respond(HttpStatusCode.BadRequest, ErrorResponse("invalid.json"))
        }

        exception<NotFoundException> { cause ->
            call.respond(HttpStatusCode.NotFound, cause.error)
        }

        exception<InternalServerException> { cause ->
            call.respond(HttpStatusCode.InternalServerError, cause.error)
        }

        exception<SocketTimeoutException> {
            call.respond(HttpStatusCode.GatewayTimeout)
        }

        exception<Exception> {
            call.respond(HttpStatusCode.InternalServerError, ErrorResponse("unexpected.server.error"))
        }

    }

    routing {
        route("/products") {
            get {
                println("products!")
                val response = ProductApiConnector(environment.config,client).getProducts()
                call.respond(response)
            }
        }
    }
}
