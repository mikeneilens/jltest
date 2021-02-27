package config

import StringOrFromTo
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.module.SimpleModule

fun ObjectMapper.jacksonConfiguration() =
    apply {
        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        enable(SerializationFeature.INDENT_OUTPUT)
        val module = SimpleModule()
        module.addDeserializer(StringOrFromTo::class.java, StringOrFromToDeserializer())
        registerModule(module)
    }

class StringOrFromToDeserializer: JsonDeserializer<StringOrFromTo>() {
    override fun deserialize(p: JsonParser, ctx: DeserializationContext):StringOrFromTo {
        val node: JsonNode = p.getCodec().readTree(p)
        return nodeToStringOrFromTo(node)
    }
}

fun nodeToStringOrFromTo(node: JsonNode): StringOrFromTo {
    val string = node.textValue()
    return if (string != null) {
        nodeToString(string)
    } else {
        nodeToFromTo(node.get("from"),node.get("to"))
    }
}

private fun nodeToString(string: String) =
    if (string.isEmpty()) StringOrFromTo.Empty else StringOrFromTo.String(string)

private fun nodeToFromTo(from:JsonNode, to:JsonNode) =
    if (from != null && to != null) StringOrFromTo.FromTo(from.textValue(), to.textValue())
    else StringOrFromTo.String("")

