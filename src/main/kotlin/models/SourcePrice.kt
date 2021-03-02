data class SourcePrice(
    val was:PriceType,
    val then1:PriceType,
    val then2:PriceType,
    val now:PriceType,
    val currency:String
) {
    fun isReduced() = was is PriceType.Single || was is PriceType.FromTo
}

