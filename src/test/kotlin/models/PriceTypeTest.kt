import PriceType
import io.kotest.core.spec.style.WordSpec
import io.kotest.data.row
import io.kotest.matchers.shouldBe

class PriceTypeTest: WordSpec({
    "priceType" should({
        "display as an empty string when it is an Empty type" {
            "${PriceType.Empty}" shouldBe ""
        }
        "display as a numerical value when it is a single value type" {
            "${PriceType.Single(1.2)}" shouldBe "1.20"
        }
        "display as a pair of numerical values seperated by a dash when it is a from/to value type" {
            "${PriceType.FromTo(15.0,9.5)}" shouldBe "15 - 9.50"
        }
        "display the invalid value when it is an invalid value type" {
            "${PriceType.Invalid("invalid")}" shouldBe "invalid"
        }
        "be equal to another PriceType only when both have the same type and all the values match" {
            listOf(
                row(PriceType.Single(10.0), PriceType.Single(10.0), true),
                row(PriceType.Single(10.0), PriceType.Single(60.0), false),
                row(PriceType.FromTo(10.0, 20.0), PriceType.FromTo(10.0, 20.0), true),
                row(PriceType.FromTo(10.0, 20.0), PriceType.FromTo(15.0, 20.0),false),
                row(PriceType.FromTo(10.0, 20.0), PriceType.FromTo(10.0, 30.0),false),
                row(PriceType.Single(10.0), PriceType.FromTo(20.0, 10.0),false),
                row(PriceType.Empty,                PriceType.Empty,               true),
                row(PriceType.Single(0.0),    PriceType.Empty,              false),
                row(PriceType.Invalid("abc"), PriceType.Invalid("abc"),true),
                row(PriceType.Invalid("abc"), PriceType.Invalid("ac"),false),
            ).forEach { (firstPriceType, secondPriceType, expectedResult) ->
                (firstPriceType == secondPriceType) shouldBe expectedResult
            }
        }

    })
})