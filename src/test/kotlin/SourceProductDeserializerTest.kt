import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class Now (@JsonProperty("now") val now:StringOrFromTo)

val jacksonMapper = ObjectMapper().registerModule(KotlinModule())

class SourceProductDeserializerTest {

    @Test
    fun `deserializing a StringOrFromTo returns the string value if the json value is a string`() {
        val json = """
            {
            "now":"123.456" 
            }
            """.trimIndent()
        jacksonMapper.jacksonConfiguration()
        val result = jacksonMapper.readValue<Now>(json).now as StringOrFromTo.String
        assertEquals("123.456", result.value)
    }
    @Test
    fun `deserializing a StringOrFromTo returns the from and to values if the json value is a from to object`() {
        val json = """
            {
            "now":{"from":"123.4","to":"567.8"} 
            }
            """.trimIndent()
        jacksonMapper.jacksonConfiguration()
        val result = jacksonMapper.readValue<Now>(json).now as StringOrFromTo.FromTo
        assertEquals("123.4", result.from)
        assertEquals("567.8", result.to)
    }
    @Test
    fun `deserializing a StringOrFromTo returns empty string if the json value is an object not containing from or to keys`() {
        val json = """
            {
            "now":{"f":"123.4","t":"567.8"} 
            }
            """.trimIndent()
        jacksonMapper.jacksonConfiguration()
        val result = jacksonMapper.readValue<Now>(json).now as StringOrFromTo.String
        assertEquals("", result.value)
    }
}

