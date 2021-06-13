import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import priceFormatter
import kotlin.test.assertEquals

class PriceFormatterTest: WordSpec({
    "When given a double priceFormatter " should ({
        "create a price with two decimals when the number is below ten" {
            2.34.priceFormatter() shouldBe "2.34"
        }
        "create a price with two decimals when the number is above ten and contains decimals" {
            10.34.priceFormatter() shouldBe "10.34"
        }
        "create a price with no decimals when the number is equal to ten" {
            10.00.priceFormatter() shouldBe "10"
        }
        "create a price with two decimals when the number is below ten and contains no decimals" {
            2.0.priceFormatter() shouldBe "2.00"
        }
        "create a price with no decimals when the number is greater to ten with no decimals" {
            11.00.priceFormatter() shouldBe "11"
        }
    })
})
