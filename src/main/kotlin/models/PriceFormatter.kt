import java.text.DecimalFormat

fun Double.priceFormatter(): String {
    val numberWithDecimals = DecimalFormat("#.00").format(this)
    if (this < 10) return numberWithDecimals
    return if (numberWithDecimals.contains(".00")) numberWithDecimals.removeSuffix(".00") else numberWithDecimals
}
