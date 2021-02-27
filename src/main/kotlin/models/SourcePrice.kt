data class SourcePrice(
    val was:PriceType,
    val then1:PriceType,
    val then2:PriceType,
    val now:PriceType,
    val currency:String
)

sealed class PriceType {
    class Single(val value: Double):PriceType()
    class FromTo(val from: Double, val to: Double):PriceType()
    object Empty:PriceType()

    override fun toString() =
        when(this) {
            is Single -> value.priceFormatter()
            is FromTo -> "${from.priceFormatter()} - ${to.priceFormatter()}"
            is Empty -> ""
        }

    override fun equals(other: Any?): Boolean =
        when(this) {
            is Single -> (other is Single && this.value == other.value)
            is FromTo -> (other is FromTo && this.from == other.from && this.to == other.to)
            is Empty -> (other is Empty)
        }
}
