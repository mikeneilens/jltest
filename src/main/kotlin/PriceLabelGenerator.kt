import kotlin.math.roundToInt

fun SourcePrice.priceLabelGenerator(labelType:LabelType) =
    when(labelType) {
        LabelType.ShowWasNow, LabelType.None -> createShowWasNow()
        LabelType.ShowWasThenNow -> createShowWasThenNow()
        LabelType.ShowPercDiscount -> createShowPercDiscount()
    }


fun SourcePrice.createShowWasNow() = "Was $displayCurrency$was, now $displayCurrency$now"

fun SourcePrice.createShowWasThenNow():String {
    if (then2.isEmpty() && then1.isEmpty()) return createShowWasNow()
    val thenPrice = if(then2.isEmpty()) then1 else then2
    return "Was $displayCurrency$was, then $displayCurrency$thenPrice now $displayCurrency$now"
}

fun SourcePrice.createShowPercDiscount():String {
    val originalWas = when(was) {
        is StringOrFromTo.String -> was.value
        is StringOrFromTo.FromTo -> was.from
    }
    val originalNow = when(now) {
        is StringOrFromTo.String -> now.value
        is StringOrFromTo.FromTo -> now.from
    }
    val wasDouble = originalWas.toDoubleOrNull() ?: return "No Was"
    val nowDouble = originalNow.toDoubleOrNull() ?: return "No Now"
    val percDiscount = ((wasDouble - nowDouble)/wasDouble * 100.00).roundToInt()
    return "$percDiscount% off - now $displayCurrency$now"
}

private val SourcePrice.displayCurrency get() = currencyMap[currency]
