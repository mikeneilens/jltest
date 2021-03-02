package config

import PriceType
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.module.SimpleModule

fun ObjectMapper.jacksonConfiguration() =
    apply {
        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
        enable(SerializationFeature.INDENT_OUTPUT)
        val module = SimpleModule()
        module.addDeserializer(PriceType::class.java, StringOrFromToDeserializer())
        registerModule(module)
    }

class StringOrFromToDeserializer: JsonDeserializer<PriceType>() {
    override fun deserialize(p: JsonParser, ctx: DeserializationContext):PriceType {
        val node: JsonNode = p.getCodec().readTree(p)
        return nodeToStringOrFromTo(node)
    }
}

fun nodeToStringOrFromTo(node: JsonNode): PriceType {
    val string = node.textValue()
    return if (string != null) {
        nodeToString(string)
    } else {
        nodeToFromTo(node.get("from"),node.get("to"))
    }
}

private fun nodeToString(string: String) =
    if (string.isEmpty()) PriceType.Empty
    else string.toPositiveDoubleOrNull()?.let{PriceType.Single(it)} ?: PriceType.Invalid(string)

private fun nodeToFromTo(from:JsonNode?, to:JsonNode?):PriceType {
    return if (from != null && to != null) {
        val fromPrice = from.textValue().toPositiveDoubleOrNull() ?: return PriceType.Invalid(from.textValue())
        val toPrice = to.textValue().toPositiveDoubleOrNull() ?: return PriceType.Invalid(to.textValue())
        PriceType.FromTo(fromPrice, toPrice)
    }
    else PriceType.Empty
}

fun String.toPositiveDoubleOrNull():Double? = toDoubleOrNull()?.let{ if (it >= 0) it else null} ?: null



