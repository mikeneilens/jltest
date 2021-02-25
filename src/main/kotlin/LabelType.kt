enum class LabelType {
    ShowWasNow, ShowWasThenNow, ShowPercDiscount, None;
}

fun String?.toLabelType() = when(this) {
    "ShowWasNow" -> LabelType.ShowWasNow
    "ShowWasThenNow" -> LabelType.ShowWasThenNow
    "ShowPercDiscount" -> LabelType.ShowPercDiscount
    else -> LabelType.None
}
