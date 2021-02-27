package config

import PriceType
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.Test
import kotlin.test.assertEquals


class JacksonConfigurationTest {

    @Test
    fun `reading a json node containing a string into PriceType Single type`() {
        val json = """{
            "was":"1.20"
            }
        """.trimIndent()
        val objectMapper = ObjectMapper()
        val wasNode = objectMapper.readTree(json).get("was")
       assertEquals(PriceType.Single(1.20),nodeToStringOrFromTo(wasNode))
    }
    @Test
    fun `reading a json node containing an empty string into PriceType Empty type`() {
        val json = """{
            "was":""
            }
        """.trimIndent()
        val objectMapper = ObjectMapper()
        val wasNode = objectMapper.readTree(json).get("was")
        assertEquals(PriceType.Empty,nodeToStringOrFromTo(wasNode))
    }
    @Test
    fun `reading a json node containing a from to object into Price Type FromTo type`() {
        val json = """{
            "was":{"from":"1.20","to":"1.00"}
            }
        """.trimIndent()
        val objectMapper = ObjectMapper()
        val wasNode = objectMapper.readTree(json).get("was")
        assertEquals(PriceType.FromTo(1.20, 1.00),nodeToStringOrFromTo(wasNode))
    }
    @Test
    fun `reading a json node containing an invalid string into PriceType Invalid type`() {
        val json = """{
            "was":"abc"
            }
        """.trimIndent()
        val objectMapper = ObjectMapper()
        val wasNode = objectMapper.readTree(json).get("was")
        assertEquals(PriceType.Invalid("abc"),nodeToStringOrFromTo(wasNode))
    }
    @Test
    fun `reading a json node containing an negative value into PriceType Invalid type`() {
        val json = """{
            "was":"-1"
            }
        """.trimIndent()
        val objectMapper = ObjectMapper()
        val wasNode = objectMapper.readTree(json).get("was")
        assertEquals(PriceType.Invalid("-1"),nodeToStringOrFromTo(wasNode))
    }
}