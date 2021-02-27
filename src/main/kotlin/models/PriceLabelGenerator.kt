import kotlin.math.roundToInt

val priceLabelGenerator = mapOf(
    "ShowWasNow" to SourcePrice::createShowWasNow,
    "ShowWasThenNow" to SourcePrice::createShowWasThenNow,
    "ShowPercDiscount" to SourcePrice::createShowPercDiscount,
)

fun priceLabelGenerator(labelType:String) = priceLabelGenerator[labelType] ?: SourcePrice::createShowWasNow

fun SourcePrice.createShowWasNow() = if (was is PriceType.Empty) createShowNow() else "Was $displayCurrency$was, now $displayCurrency$now"

fun SourcePrice.createShowWasThenNow():String {
    if (was is PriceType.Empty) return createShowNow() else
    if (then2 is PriceType.Empty && then1 is PriceType.Empty) return createShowWasNow()
    val thenPrice = if(then2 is PriceType.Empty) then1 else then2
    return "Was $displayCurrency${was}, then $displayCurrency$thenPrice now $displayCurrency$now"
}

fun SourcePrice.createShowPercDiscount():String {
    if (was.minValue == 0.0) return "No Was"
    if (now.minValue == 0.0) return "No Now"
    val percDiscount = ((was.minValue - now.minValue)/was.minValue * 100.00).roundToInt()
    return "$percDiscount% off - now $displayCurrency$now"
}

fun SourcePrice.createShowNow(): String = "Now $displayCurrency$now"

private val PriceType.minValue get()  =
    when (this) {
        is PriceType.Single -> value
        is PriceType.FromTo -> from
        is PriceType.Empty, is PriceType.Invalid -> 0.0
    }

private val SourcePrice.displayCurrency get() = currencyMap[currency]
