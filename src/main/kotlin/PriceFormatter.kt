import java.text.DecimalFormat

fun String.priceFormatter(): String {
    val value = toDoubleOrNull() ?: return this
    val numberWithDecimals = DecimalFormat("#.00").format(value)
    if (value < 10) return numberWithDecimals
    return if (numberWithDecimals.contains(".00")) numberWithDecimals.removeSuffix(".00") else numberWithDecimals
}
