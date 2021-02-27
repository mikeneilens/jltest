import kotlin.math.roundToInt

fun SourcePrice.priceLabelGenerator(labelType:LabelType) =
    if (was is PriceType.Empty) createNow()
    else
    when(labelType) {
        LabelType.ShowWasNow, LabelType.None -> createShowWasNow()
        LabelType.ShowWasThenNow -> createShowWasThenNow()
        LabelType.ShowPercDiscount -> createShowPercDiscount()
    }

fun SourcePrice.createShowWasNow() = "Was $displayCurrency$was, now $displayCurrency$now"

fun SourcePrice.createShowWasThenNow():String {
    if (then2 is PriceType.Empty && then1 is PriceType.Empty) return createShowWasNow()
    val thenPrice = if(then2 is PriceType.Empty) then1 else then2
    return "Was $displayCurrency${was}, then $displayCurrency$thenPrice now $displayCurrency$now"
}

fun SourcePrice.createShowPercDiscount():String {
    val originalWas = when(was) {
        is PriceType.Single -> was.value
        is PriceType.FromTo -> was.from
        is PriceType.Empty -> 0.0
    }
    val originalNow = when(now) {
        is PriceType.Single -> now.value
        is PriceType.FromTo -> now.from
        is PriceType.Empty -> 0.0
    }
    if (originalWas == 0.0) return "No Was"
    if (originalNow == 0.0) return "No Now"
    val percDiscount = ((originalWas - originalNow)/originalWas * 100.00).roundToInt()
    return "$percDiscount% off - now $displayCurrency$now"
}

fun SourcePrice.createNow(): String = "Now $displayCurrency$now"

private val SourcePrice.displayCurrency get() = currencyMap[currency]
