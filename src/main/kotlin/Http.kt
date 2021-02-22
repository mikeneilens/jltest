import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.json.*

fun client() = HttpClient (OkHttp){
    install(JsonFeature) {
        serializer = JacksonSerializer(jacksonObjectMapper(), ::jacksonConfiguration)
    }
}