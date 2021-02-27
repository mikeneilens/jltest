import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class PriceLabelGeneratorTest {
    @Test
    fun `generating a pricelabel when labelType is ShowWasNow`() {
        val price = SourcePrice(PriceType.Single(2.00), PriceType.Single(1.50), PriceType.Single(1.20), PriceType.Single(1.00), "GBP")
        assertEquals("Was £2.00, now £1.00", price.priceLabelGenerator(LabelType.ShowWasNow))
    }
    @Test
    fun `generating a pricelabel when labelType is ShowWasThenNow`() {
        val price = SourcePrice(PriceType.Single(2.00), PriceType.Single(1.50), PriceType.Single(1.25), PriceType.Single(1.00), "GBP")
        assertEquals("Was £2.00, then £1.25 now £1.00", price.priceLabelGenerator(LabelType.ShowWasThenNow))
    }
    @Test
    fun `generating a pricelabel when labelType is ShowPercDiscount`() {
        val price = SourcePrice(PriceType.Single(2.00), PriceType.Single(1.50), PriceType.Empty, PriceType.Single(1.00), "GBP")
        assertEquals("50% off - now £1.00", price.priceLabelGenerator(LabelType.ShowPercDiscount))
    }
    @Test
    fun `generating a pricelabel when labelType is None`() {
        val price = SourcePrice(PriceType.Single(2.00), PriceType.Single(1.50), PriceType.Single(1.20), PriceType.Single(1.00), "GBP")
        assertEquals("Was £2.00, now £1.00", price.priceLabelGenerator(LabelType.None))
    }

    @Test
    fun `converting a price to a priceLabel when there is an empty was`() {
        val price = SourcePrice(PriceType.Empty, PriceType.Single(1.50), PriceType.Single(1.20), PriceType.Single(1.00), "GBP")
        assertEquals("Now £1.00", price.createNow())
    }

    @Test
    fun `converting a price to a priceLabel when there is a single now price and labelType is ShowWasNow`() {
        val price = SourcePrice(PriceType.Single(2.00), PriceType.Single(1.50), PriceType.Single(1.20), PriceType.Single(1.00), "GBP")
        assertEquals("Was £2.00, now £1.00", price.createShowWasNow())
    }
    @Test
    fun `converting a price to a priceLabel when there is a from-to now price and labelType is ShowWasNow`() {
        val price = SourcePrice(
            PriceType.FromTo(2.00, 2.50),
            PriceType.Single(1.50),
            PriceType.Single(1.20),
            PriceType.FromTo(1.00, 1.25),
            "GBP"
        )
        assertEquals("Was £2.00 - 2.50, now £1.00 - 1.25", price.createShowWasNow())
    }

    @Test
    fun `converting a price to a priceLabel when there is a single now price and no then prices and labelType is ShowWasThenNow`() {
        val price = SourcePrice(PriceType.Single(2.00), PriceType.Empty, PriceType.Empty, PriceType.Single(1.00), "GBP")
        assertEquals("Was £2.00, now £1.00", price.createShowWasThenNow())
    }
    @Test
    fun `converting a price to a priceLabel when there is a sngle now price and no then2 price and labelType is ShowWasThenNow`() {
        val price = SourcePrice(PriceType.Single(2.00), PriceType.Single(1.50), PriceType.Empty, PriceType.Single(1.00), "GBP")
        assertEquals("Was £2.00, then £1.50 now £1.00", price.createShowWasThenNow())
    }
    @Test
    fun `converting a price to a priceLabel when there is a single price and then2 price exists and labelType is ShowWasThenNow`() {
        val price = SourcePrice(PriceType.Single(2.00), PriceType.Single(1.50), PriceType.Single(1.25), PriceType.Single(1.00), "GBP")
        assertEquals("Was £2.00, then £1.25 now £1.00", price.createShowWasThenNow())
    }
    @Test
    fun `converting a price to a priceLabel when there is from-to now price and then2 price exists and labelType is ShowWasThenNow`() {
        val price =
            SourcePrice(PriceType.FromTo(2.00, 2.50), PriceType.Single(1.50), PriceType.Single(1.25), PriceType.Single(1.00), "GBP")
        assertEquals("Was £2.00 - 2.50, then £1.25 now £1.00", price.createShowWasThenNow())
    }

    @Test
    fun `converting a price to a priceLabel when there is a single price and labelType is ShowPercDscount`() {
        val price = SourcePrice(PriceType.Single(2.00), PriceType.Single(1.50), PriceType.Empty, PriceType.Single(1.00), "GBP")
        assertEquals("50% off - now £1.00", price.createShowPercDiscount())
    }
    @Test
    fun `converting a price to a priceLabel uses lower was now prices when there is two now prices and labelType is ShowPercDscount`() {
        val price =
            SourcePrice(PriceType.FromTo(2.00, 2.50), PriceType.Single(1.50), PriceType.Empty, PriceType.FromTo(1.00, 1.20), "GBP")
        assertEquals("50% off - now £1.00 - 1.20", price.createShowPercDiscount())
    }
    @Test
    fun `converting a price to a priceLabel when there is an invalid was or now price and labelType is ShowPercDscount`() {
        val invalidWasPrice =
            SourcePrice(PriceType.Empty, PriceType.Single(1.50), PriceType.Empty, PriceType.Single(1.00), "GBP")
        assertEquals("No Was", invalidWasPrice.createShowPercDiscount())
        val invalidNowPrice =
            SourcePrice(PriceType.Single(2.00), PriceType.Single(1.50), PriceType.Empty, PriceType.Single(0.0), "GBP")
        assertEquals("No Now", invalidNowPrice.createShowPercDiscount())
    }
}