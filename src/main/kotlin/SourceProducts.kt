
data class SourceProducts(val products:List<SourceProduct>) {
    fun toProducts(labelType:LabelType, priceLabelGenerator:(SourcePrice, LabelType)->String) = ReturnedProducts(products.map{it.toProduct(labelType, priceLabelGenerator)})
}

data class SourceProduct(
    val productId:String,
    val title:String,
    val colorSwatches:List<SourceColorSwatch>,
    val price:SourcePrice) {

    fun toProduct(labelType:LabelType, priceLabelGenerator:(SourcePrice, LabelType)->String):ReturnedProduct {
        val now  = when(price.now) {
            is StringOrFromTo.String -> price.now.value.priceFormatter()
            is StringOrFromTo.FromTo -> price.now.from.priceFormatter()
            is StringOrFromTo.Empty -> ""
        }
        val returnedSwatches = colorSwatches.map(SourceColorSwatch::toColorSwatch)
        return ReturnedProduct(productId, title,returnedSwatches,now,priceLabelGenerator(price,labelType))
    }
}

data class SourceColorSwatch(
    val color:String,
    val basicColor:String,
    val skuId:String
) {
    fun toColorSwatch() = ColorSwatch(color, basicColor.toRgbColor(), skuId)
}

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
    object Empty:StringOrFromTo()

    override fun toString() =
        when(this) {
            is String -> value.priceFormatter()
            is FromTo -> "${from.priceFormatter()} - ${to.priceFormatter()}"
            is Empty -> ""
        }

    override fun equals(other: Any?): Boolean =
        when(this) {
            is String -> (other is String && this.value == other.value)
            is FromTo -> (other is FromTo && this.from == other.from && this.to == other.to)
            is Empty -> (other is Empty)
        }

}
