import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import config.jacksonConfiguration
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.json.*

fun client() = HttpClient (OkHttp){
    install(JsonFeature) {
        serializer = JacksonSerializer(jacksonObjectMapper(), ObjectMapper::jacksonConfiguration)
    }
    expectSuccess = false
}