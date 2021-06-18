import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.config.*
import io.ktor.http.*

class ProductApiConnector (
    val config: ApplicationConfig,
    private val client: HttpClient
) {
    private val env = config.property("ktor.environment").getString()
    private val baseUrl =  config.property("services.$env.jl.url").getString()
    private val key =  config.property("services.$env.jl.key").getString()

    suspend fun getProducts():SourceProducts =
        try {
            client.get<HttpResponse>("$baseUrl&$key").handle()
        } catch (e: MissingKotlinParameterException) {
            throw (BadRequestException(ErrorResponse("n/a","n/a")))
        }
}

suspend inline fun HttpResponse.handle(): SourceProducts =
    if (status.isSuccess()) {
         receive()
    } else {
        throw (BadRequestException(ErrorResponse("n/a","n/a")))
    }



