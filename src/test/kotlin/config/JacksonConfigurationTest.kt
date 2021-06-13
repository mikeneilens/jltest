package config

import PriceType
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe

data class TestPrice (val price:PriceType)

class NewJacksonConfigurationTest: WordSpec( {
    "When given json ObjectMapper " should ({
        "deserialises into a PriceType when json contains a price"  {
            val json = """{
            "price":"1.20"
            }
        """.trimIndent()
            val objectMapper = jacksonObjectMapper().jacksonConfiguration()

            objectMapper.readValue<TestPrice>(json) shouldBe TestPrice(PriceType.Single(1.20))
        }

    })
    "When reading a json node ObjectMapper " should ({
        "creates a Single type when reading a json node containing a string of a positive number into PriceType" {
            val json = """{
            "was":"1.20"
            }
        """.trimIndent()
            val objectMapper = ObjectMapper()
            val wasNode = objectMapper.readTree(json).get("was")
            nodeToStringOrFromTo(wasNode) shouldBe PriceType.Single(1.20)
        }
        "Creates an Empty type when reading a json node containing an empty string into PriceType" {
            val json = """{
            "was":""
            }
        """.trimIndent()
            val objectMapper = ObjectMapper()
            val wasNode = objectMapper.readTree(json).get("was")
            nodeToStringOrFromTo(wasNode) shouldBe PriceType.Empty
        }
        "Creates a FromTo type when reading a json node containing a {from,to} object into Price Type" {
            val json = """{
            "was":{"from":"1.20","to":"1.00"}
            }
        """.trimIndent()
            val objectMapper = ObjectMapper()
            val wasNode = objectMapper.readTree(json).get("was")
            nodeToStringOrFromTo(wasNode) shouldBe PriceType.FromTo(1.20, 1.00)
        }
        "Creates a Invalid type when reading a json node containing an invalid {from,to} object into Price Type" {
            val json = """{
            "was":{"from":"abc","to":"1.00"}
            }
        """.trimIndent()
            val objectMapper = ObjectMapper()
            val wasNode = objectMapper.readTree(json).get("was")
            nodeToStringOrFromTo(wasNode) shouldBe PriceType.Invalid("abc")
        }
        "Creates an Invalid type when reading a json node containing an invalid string into PriceType" {
            val json = """{
            "was":"abc"
            }
        """.trimIndent()
            val objectMapper = ObjectMapper()
            val wasNode = objectMapper.readTree(json).get("was")
            nodeToStringOrFromTo(wasNode) shouldBe PriceType.Invalid("abc")
        }
        "Creates an Invalid type when reading a json node containing an negative value into PriceType" {
            val json = """{
            "was":"-1"
            }
        """.trimIndent()
            val objectMapper = ObjectMapper()
            val wasNode = objectMapper.readTree(json).get("was")
            nodeToStringOrFromTo(wasNode) shouldBe PriceType.Invalid("-1")
        }
    })
})