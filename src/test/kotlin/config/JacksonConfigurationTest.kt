package config

import PriceType
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.Test
import kotlin.test.assertEquals

data class TestPrice (val priceType:PriceType)

class JacksonConfigurationTest {

    @Test
    fun `ObjectMapper deserialises into a PriceType when given json containing a price`() {
        val json = """{
            "price":"1.20"
            }
        """.trimIndent()
        val objectMapper = jacksonObjectMapper().jacksonConfiguration()
        val result:TestPrice = objectMapper.readValue(json)

        assertEquals(TestPrice(PriceType.Single(1.20)), result )
    }

    @Test
    fun `reading a json node containing a string of a positive number into PriceType creates a Single type`() {
        val json = """{
            "was":"1.20"
            }
        """.trimIndent()
        val objectMapper = ObjectMapper()
        val wasNode = objectMapper.readTree(json).get("was")
       assertEquals(PriceType.Single(1.20),nodeToStringOrFromTo(wasNode))
    }
    @Test
    fun `reading a json node containing an empty string into PriceType creates an Empty type`() {
        val json = """{
            "was":""
            }
        """.trimIndent()
        val objectMapper = ObjectMapper()
        val wasNode = objectMapper.readTree(json).get("was")
        assertEquals(PriceType.Empty,nodeToStringOrFromTo(wasNode))
    }
    @Test
    fun `reading a json node containing a {from,to} object into Price Type creates a FromTo type`() {
        val json = """{
            "was":{"from":"1.20","to":"1.00"}
            }
        """.trimIndent()
        val objectMapper = ObjectMapper()
        val wasNode = objectMapper.readTree(json).get("was")
        assertEquals(PriceType.FromTo(1.20, 1.00),nodeToStringOrFromTo(wasNode))
    }
    @Test
    fun `reading a json node containing an invalid {from,to} object into Price Type creates a Invalid type`() {
        val json = """{
            "was":{"from":"abc","to":"1.00"}
            }
        """.trimIndent()
        val objectMapper = ObjectMapper()
        val wasNode = objectMapper.readTree(json).get("was")
        assertEquals(PriceType.Invalid("abc"),nodeToStringOrFromTo(wasNode))
    }
    @Test
    fun `reading a json node containing an invalid string into PriceType creates an Invalid type`() {
        val json = """{
            "was":"abc"
            }
        """.trimIndent()
        val objectMapper = ObjectMapper()
        val wasNode = objectMapper.readTree(json).get("was")
        assertEquals(PriceType.Invalid("abc"),nodeToStringOrFromTo(wasNode))
    }
    @Test
    fun `reading a json node containing an negative value into PriceType creates an Invalid type`() {
        val json = """{
            "was":"-1"
            }
        """.trimIndent()
        val objectMapper = ObjectMapper()
        val wasNode = objectMapper.readTree(json).get("was")
        assertEquals(PriceType.Invalid("-1"),nodeToStringOrFromTo(wasNode))
    }
}