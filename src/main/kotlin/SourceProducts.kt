import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.JsonNode

data class SourceProducts(
    val products:List<SourceProduct>
)

data class SourceProduct(
    val productId:String,
    val title:String,
    val colorSwatches:List<SourceColorSwatch>,
    val price:SourcePrice
) {
    fun toProduct() = Product(productId)
}

data class SourceColorSwatch(
    val color:String,
    val basicColor:String,
    val skuId:String
)

data class SourcePrice(
    val was:StringOrFromTo,
    val then1:String,
    val then2:String,
    val now:StringOrFromTo,
    val currency:String
)

sealed class StringOrFromTo {
    class String(val value: kotlin.String):StringOrFromTo()
    class FromTo(val from: kotlin.String, val to: kotlin.String):StringOrFromTo()

    override fun toString() =
        when(this) {
            is String -> value
            is FromTo -> "from: $from to: $to"
        }
}
