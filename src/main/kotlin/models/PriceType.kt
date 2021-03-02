sealed class PriceType {
    class Single(val value: Double):PriceType()
    class FromTo(val from: Double, val to: Double):PriceType()
    object Empty:PriceType()
    class Invalid(val value:String):PriceType()

    override fun toString() =
        when(this) {
            is Single -> value.priceFormatter()
            is FromTo -> "${from.priceFormatter()} - ${to.priceFormatter()}"
            is Invalid -> value
            is Empty -> ""
        }

    override fun equals(other: Any?): Boolean =
        when(this) {
            is Single -> (other is Single && this.value == other.value)
            is FromTo -> (other is FromTo && this.from == other.from && this.to == other.to)
            is Invalid -> (other is Invalid && this.value == other.value)
            is Empty -> (other is Empty)
        }
}