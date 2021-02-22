import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.config.*

class ProductApiConnector (
    val config: ApplicationConfig,
    private val client: HttpClient
) {
    private val env = config.property("ktor.environment").getString()
    private val baseUrl =  config.property("services.$env.jl.url").getString()

    suspend fun getProducts():SourceProducts = client.get<HttpResponse>(baseUrl).receive()
}

data class SourceProduct(
    val productId:String
    )

data class SourceProducts(
    val products:List<SourceProduct>
    )