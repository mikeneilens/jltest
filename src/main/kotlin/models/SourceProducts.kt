
data class SourceProducts(val products:List<SourceProduct>) {
    fun toProducts(labelType:LabelType, priceLabelGenerator:(SourcePrice, LabelType)->String) = Products(products.map{it.toProduct(labelType, priceLabelGenerator)})
}

data class SourceProduct(
    val productId:String,
    val title:String,
    val colorSwatches:List<SourceColorSwatch>,
    val price:SourcePrice) {

    fun toProduct(labelType:LabelType, priceLabelGenerator:(SourcePrice, LabelType)->String):Product {
        val now  = when(price.now) {
            is StringOrFromTo.String -> price.now.value.priceFormatter()
            is StringOrFromTo.FromTo -> price.now.from.priceFormatter()
            is StringOrFromTo.Empty -> ""
        }
        val returnedSwatches = colorSwatches.map(SourceColorSwatch::toColorSwatch)
        return Product(productId, title,returnedSwatches,now,priceLabelGenerator(price,labelType))
    }
}

data class SourceColorSwatch(
    val color:String,
    val basicColor:String,
    val skuId:String
) {
    fun toColorSwatch() = ColorSwatch(color, basicColor.toRgbColor(), skuId)
}
