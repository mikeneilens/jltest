
data class ReducedProducts (val products:List<ReducedProduct>)

data class ReducedProduct (
    val productId:String,
    val title:String,
    val colorSwatches:List<ColorSwatch>,
    val nowPrice:String,
    val priceLabel:String
    )

data class ColorSwatch (
    val color:String,
    val rgbColor:String,
    val skuId:String
)

