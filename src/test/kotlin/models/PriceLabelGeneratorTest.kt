import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe

class PriceLabelGeneratorTest: WordSpec( {

    "obtainPriceLabelGenerator" should ({
        "create a priceLabelGenerator that can be used to create a Was/Now label" {
            val price = SourcePrice(PriceType.Single(2.00), PriceType.Single(1.50), PriceType.Single(1.20), PriceType.Single(1.00), "GBP")
            obtainPriceLabelGenerator("ShowWasNow")(price) shouldBe "Was £2.00, now £1.00"
        }

        "create a priceLabelGenerator that can be used to create a Was/then/Now label" {
            val price = SourcePrice(PriceType.Single(2.00), PriceType.Single(1.50), PriceType.Single(1.25), PriceType.Single(1.00), "GBP")
            obtainPriceLabelGenerator("ShowWasThenNow")(price) shouldBe "Was £2.00, then £1.25 now £1.00"
        }

        "create a priceLabelGenerator that can be used to create a PercDiscount label" {
            val price = SourcePrice(PriceType.Single(2.00), PriceType.Single(1.50), PriceType.Empty, PriceType.Single(1.00), "GBP")
            obtainPriceLabelGenerator("ShowPercDiscount")(price) shouldBe "50% off - now £1.00"
        }

        "create a priceLabelGenerator that can be used to create a Wsa/Now label when no pricelabel type is specified" {
            val price = SourcePrice(PriceType.Single(2.00), PriceType.Single(1.50), PriceType.Single(1.20), PriceType.Single(1.00), "GBP")
            obtainPriceLabelGenerator("")(price) shouldBe "Was £2.00, now £1.00"
        }
    })

    "CreateShowNow" should ({
        "creates a label showing now when there is an empty was" {
            val price = SourcePrice(was=PriceType.Empty, then1=PriceType.Single(1.50), then2=PriceType.Single(1.20), now=PriceType.Single(1.00), currency="GBP")
            price.createShowNow() shouldBe "Now £1.00"
        }
        "creates a label showing now when there is a was that is not empty" {
            val price = SourcePrice(was=PriceType.Single(2.50), then1=PriceType.Single(1.50), then2=PriceType.Single(1.20), now=PriceType.Single(1.00), currency="GBP")
            price.createShowNow() shouldBe "Now £1.00"
        }
        "creates a label showing an invalid now when the price contains an invalid now" {
            val price = SourcePrice(was=PriceType.Empty, then1=PriceType.Single(1.50), then2=PriceType.Single(1.20), now=PriceType.Invalid("abc"), currency="GBP")
            price.createShowNow() shouldBe "Now £abc"
        }
    })
    "createShowWasNow" should({
        "create a lable showing a single was and now price when the price contains a single was price and single now price" {
            val price = SourcePrice(was=PriceType.Single(2.00), then1=PriceType.Single(1.50), then2=PriceType.Single(1.20), now=PriceType.Single(1.00), currency="GBP")
            price.createShowWasNow() shouldBe "Was £2.00, now £1.00"
        }
        "create a label showing a range of was and now price when the price contains a from/to was price and from/to now price" {
            val price = SourcePrice(
                was=PriceType.FromTo(2.00, 2.50),
                then1=PriceType.Single(1.50),
                then2=PriceType.Single(1.20),
                now=PriceType.FromTo(1.00, 1.25),
                currency="GBP"
            )
            price.createShowWasNow() shouldBe "Was £2.00 - 2.50, now £1.00 - 1.25"
        }
    })
    "createShowWasThenNow" should({
        "create a label with no 'then' price when there is a 'was' price and a 'now' price but no 'then' price" {
            val price = SourcePrice(
                was=PriceType.Single(2.00),
                then1=PriceType.Empty,
                then2=PriceType.Empty,
                now=PriceType.Single(1.00),
                currency="GBP")
            price.createShowWasThenNow() shouldBe "Was £2.00, now £1.00"
        }
        "create a lable with a then price when there is a 'was' price and a 'then1' price and no 'then2' price" {
            val price = SourcePrice(
                was=PriceType.Single(2.00),
                then1=PriceType.Single(1.50),
                then2=PriceType.Empty,
                now=PriceType.Single(1.00),
                currency="GBP")
            price.createShowWasThenNow() shouldBe "Was £2.00, then £1.50 now £1.00"
        }
        "create a lable using the then2 price when there is a 'was' price and a 'then1' price and a 'then2' price" {
            val price = SourcePrice(
                was=PriceType.Single(2.00),
                then1=PriceType.Single(1.50),
                then2=PriceType.Single(1.25),
                now=PriceType.Single(1.00),
                currency="GBP")
            price.createShowWasThenNow() shouldBe "Was £2.00, then £1.25 now £1.00"
        }
        "create a lable with a range of was price when there is a 'was' from/to price and a 'then1' price and a 'then2' price" {
            val price = SourcePrice(
                was=PriceType.FromTo(2.00, 2.50),
                then1=PriceType.Single(1.50),
                then2=PriceType.Single(1.25),
                now=PriceType.Single(1.00),
                currency="GBP")
            price.createShowWasThenNow() shouldBe "Was £2.00 - 2.50, then £1.25 now £1.00"
        }
    })
    "showPercDiscount" should({
        "create a label showing a percentage discouint when there is a single 'was' and a single 'now' price" {
            val price = SourcePrice(
                was=PriceType.Single(2.00),
                then1=PriceType.Single(1.50),
                then2=PriceType.Empty,
                now=PriceType.Single(1.00),
                currency="GBP")
            price.createShowPercDiscount() shouldBe "50% off - now £1.00"
        }
        "creates a price label using the lower was and now prices when the 'was' and 'now' prices contain fromt/to prices" {
            val price = SourcePrice(
                was=PriceType.FromTo(2.00, 2.50),
                then1=PriceType.Single(1.50),
                then2=PriceType.Empty,
                now=PriceType.FromTo(1.00, 1.20),
                currency="GBP")
            price.createShowPercDiscount() shouldBe "50% off - now £1.00 - 1.20"
        }
        "creates a price label showing the now price when there is an empty now price" {
            val emptyWasPrice = SourcePrice(
                was=PriceType.Empty,
                then1=PriceType.Single(1.50),
                then2=PriceType.Empty,
                now=PriceType.Single(1.00),
                currency="GBP")
            emptyWasPrice.createShowPercDiscount() shouldBe "Now £1.00"

            val invalidWasPrice = SourcePrice(
                was=PriceType.Invalid("abc"),
                then1=PriceType.Single(1.50),
                then2=PriceType.Empty,
                now=PriceType.Single(1.00),
                currency="GBP")
            invalidWasPrice.createShowPercDiscount() shouldBe "No Was"

            val invalidNowPrice = SourcePrice(
                was=PriceType.Single(2.00),
                then1=PriceType.Single(1.50),
                then2=PriceType.Empty,
                now=PriceType.Invalid("abc"),
                currency="GBP")
            invalidNowPrice.createShowPercDiscount() shouldBe "No Now"
        }
    })
 })