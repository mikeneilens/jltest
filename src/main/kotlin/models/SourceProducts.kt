data class SourceProducts(val products:List<SourceProduct>) {
    fun toProducts(priceLabelGenerator:(SourcePrice)->String) =
        ReducedProducts(products.filter { it.price.isReduced() }.map { it.toProduct(priceLabelGenerator) })
}

data class SourceProduct(
    val productId:String,
    val title:String,
    val colorSwatches:List<SourceColorSwatch>,
    val price:SourcePrice) {

    fun toProduct(priceLabelGenerator:(SourcePrice)->String):ReducedProduct {
        val now  = when(price.now) {
            is PriceType.Single -> price.now.value.priceFormatter()
            is PriceType.FromTo -> price.now.from.priceFormatter()
            is PriceType.Invalid -> price.now.value
            is PriceType.Empty -> ""
        }
        val returnedSwatches = colorSwatches.map(SourceColorSwatch::toColorSwatch)
        return ReducedProduct(productId, title,returnedSwatches,now,priceLabelGenerator(price))
    }
}

data class SourceColorSwatch(
    val color:String,
    val basicColor:String,
    val skuId:String
) {
    fun toColorSwatch() = ColorSwatch(color, basicColor.toRgbColor(), skuId)
}
