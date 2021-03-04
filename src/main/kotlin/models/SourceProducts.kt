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
        val reducedSwatches = colorSwatches.map(SourceColorSwatch::toColorSwatch)
        return ReducedProduct(productId, title, reducedSwatches, price.now.toString(), priceLabelGenerator(price))
    }
}

data class SourceColorSwatch(
    val color:String,
    val basicColor:String,
    val skuId:String
) {
    fun toColorSwatch() = ColorSwatch(color, basicColor.toRgbColor(), skuId)
}
