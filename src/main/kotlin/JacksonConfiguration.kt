import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature

fun ObjectMapper.jacksonConfiguration() =
    apply {
        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        enable(SerializationFeature.INDENT_OUTPUT)
    }