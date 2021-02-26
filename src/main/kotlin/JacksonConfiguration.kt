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
        return stringOrFromTo(node)
    }
}

fun stringOrFromTo(node: JsonNode): StringOrFromTo {
    val string = node.textValue()
    return if (string != null) {
        if (string.isEmpty()) StringOrFromTo.Empty
        else StringOrFromTo.String(string)
    } else {
        val from = node.get("from")
        val to = node.get("to")
        if (from != null && to != null) StringOrFromTo.FromTo(from.textValue(), to.textValue())
        else StringOrFromTo.String("")
    }
}
