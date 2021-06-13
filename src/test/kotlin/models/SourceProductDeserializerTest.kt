import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.readValue
import config.jacksonConfiguration
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.should
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.beInstanceOf

class Now (@JsonProperty("now") val now:PriceType)

val jacksonMapper = ObjectMapper().registerModule(KotlinModule())

class SourceProductDeserializerTest:WordSpec({

    "deserialising a StringOrFromTo" should ({
        "create a priceType with a single value if the json is a single value" {
            val json = """
            {
            "now":"123.456" 
            }
            """.trimIndent()
            jacksonMapper.jacksonConfiguration()
            val result = jacksonMapper.readValue<Now>(json).now as PriceType.Single
            result.value shouldBe 123.456
        }

        "create a priceType with a from/to value if the json has a from and to value" {
            val json = """
            {
            "now":{"from":"123.4","to":"567.8"} 
            }
            """.trimIndent()
            jacksonMapper.jacksonConfiguration()
            val result = jacksonMapper.readValue<Now>(json).now as PriceType.FromTo
            result.from shouldBe 123.4
            result.to shouldBe 567.8
        }
        "create a priceType with an empty type if the json has an invalid set of values" {
            val json = """
            {
            "now":{"f":"123.4","t":"567.8"} 
            }
            """.trimIndent()
            jacksonMapper.jacksonConfiguration()
            val result = jacksonMapper.readValue<Now>(json).now
            result should beInstanceOf<PriceType.Empty>()
        }

    })
})

