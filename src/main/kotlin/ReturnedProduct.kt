import kotlin.math.roundToInt

data class ReturnedProducts (val products:List<ReturnedProduct>)

data class ReturnedProduct (
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

fun String.toRgbColor():String = colorMap[this] ?: "unknown"
