import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
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
    private val key =  config.property("services.$env.jl.key").getString()

    val jacksonMapper = ObjectMapper().registerModule(KotlinModule())
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)

    suspend fun getProducts():SourceProducts {
       val data = client.get<String>("$baseUrl&$key")
        val module = SimpleModule()
        module.addDeserializer(StringOrFromTo::class.java, StringOrFromToDeserializer())
        jacksonMapper.registerModule(module)
        val products:SourceProducts = jacksonMapper.readValue(data)
        println(products)
        return products
    }
}


