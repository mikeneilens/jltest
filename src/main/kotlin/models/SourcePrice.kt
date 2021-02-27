data class SourcePrice(
    val was:StringOrFromTo,
    val then1:String,
    val then2:String,
    val now:StringOrFromTo,
    val currency:String
)

sealed class StringOrFromTo {
    class String(val value: kotlin.String):StringOrFromTo()
    class FromTo(val from: kotlin.String, val to: kotlin.String):StringOrFromTo()
    object Empty:StringOrFromTo()

    override fun toString() =
        when(this) {
            is String -> value.priceFormatter()
            is FromTo -> "${from.priceFormatter()} - ${to.priceFormatter()}"
            is Empty -> ""
        }

    override fun equals(other: Any?): Boolean =
        when(this) {
            is String -> (other is String && this.value == other.value)
            is FromTo -> (other is FromTo && this.from == other.from && this.to == other.to)
            is Empty -> (other is Empty)
        }
}
