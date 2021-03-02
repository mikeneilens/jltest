import com.fasterxml.jackson.module.kotlin.readValue
import config.jacksonConfiguration
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class SourceProductTest {

    @Test
    fun `decoding product json results in a SourceProduct object containing the correct data`(){
        jacksonMapper.jacksonConfiguration()
        val result = jacksonMapper.readValue<SourceProducts>(testJson)

        val expectedResult = SourceProducts(
            listOf(
                SourceProduct(
                    "productId1", "title1",
                    listOf(SourceColorSwatch("color1", "basicColor1", "skuId1")),
                    SourcePrice(PriceType.Single(1.00), PriceType.Single(2.0), PriceType.Single(3.0), PriceType.Single(4.0), "GBP")
                ),
                SourceProduct(
                    "productId2", "title2",
                    listOf(SourceColorSwatch("color2", "basicColor2", "skuId2")),
                    SourcePrice(
                        PriceType.FromTo(11.0, 12.0),
                        PriceType.Single(13.0),
                        PriceType.Single(14.0),
                        PriceType.FromTo(15.0, 16.0),
                        "USD"
                    )
                )
            )
        )
        assertEquals(expectedResult, result)
    }
    @Test
    fun `converting a sourceProduct to a reduced product when the now price has one value`() {
        fun SourcePrice.mockPriceLabelGenerator() = "price is $now"

        val sourceProduct = SourceProduct(
            "productId1", "title1",
            listOf(SourceColorSwatch("color1", "Red", "skuId1")),
            SourcePrice(PriceType.Single(1.00), PriceType.Single(2.0), PriceType.Single(3.0), PriceType.Single(4.0), "GBP")
        )

        val expectedResult = ReducedProduct(
            "productId1", "title1",
            listOf(ColorSwatch("color1", "FF0000", "skuId1")),
            "4.00", "price is 4.00"
        )

        assertEquals(expectedResult, sourceProduct.toProduct(SourcePrice::mockPriceLabelGenerator))
    }
    @Test
    fun `converting a sourceProduct to a reduced product when the now price has two values the now price shows from to`() {
        fun SourcePrice.mockPriceLabelGenerator() = "price is $now"

        val sourceProduct = SourceProduct(
            "productId1", "title1",
            listOf(SourceColorSwatch("color1", "Red", "skuId1")),
            SourcePrice(PriceType.Single(1.0), PriceType.Single(2.0), PriceType.Single(3.0), PriceType.FromTo(2.0, 3.0), "GBP")
        )

        val expectedResult = ReducedProduct(
            "productId1", "title1",
            listOf(ColorSwatch("color1", "FF0000", "skuId1")),
            "2.00 - 3.00", "price is 2.00 - 3.00"
        )

        assertEquals(expectedResult, sourceProduct.toProduct(SourcePrice::mockPriceLabelGenerator))
    }
    @Test
    fun `converting a sourceProduct to a reduced product when the now price has an invalid value then the invalid price is preserved`() {
        fun SourcePrice.mockPriceLabelGenerator() = "price is $now"

        val sourceProduct = SourceProduct(
            "productId1", "title1",
            listOf(SourceColorSwatch("color1", "Red", "skuId1")),
            SourcePrice(PriceType.Single(1.0), PriceType.Single(2.0), PriceType.Single(3.0), PriceType.Invalid("two"), "GBP")
        )

        val expectedResult = ReducedProduct(
            "productId1", "title1",
            listOf(ColorSwatch("color1", "FF0000", "skuId1")),
            "two", "price is two"
        )

        assertEquals(expectedResult, sourceProduct.toProduct(SourcePrice::mockPriceLabelGenerator))
    }
    @Test
    fun `converting SourceProducts to ReducedProducts when the list of SourceProducts contains two products with only one product discounted should return one product`() {
        fun SourcePrice.mockPriceLabelGenerator() = "price was $was is now $now"

        val sourceProducts = SourceProducts(
            listOf(
                SourceProduct(
                    "productId1", "title1",
                        listOf(SourceColorSwatch("color1", "Red", "skuId1")),
                        SourcePrice(PriceType.Single(6.0), PriceType.Single(5.0), PriceType.Single(4.50), PriceType.Single(4.0), "GBP")
                ),
                SourceProduct(
                    "productId2", "title2",
                    listOf(SourceColorSwatch("color2", "Blue", "skuId1")),
                    SourcePrice(PriceType.Empty, PriceType.Single(2.0), PriceType.Single(3.0), PriceType.Single(4.5), "GBP")
                )
            )
        )

        val expectedResult = ReducedProducts(
            listOf(
                ReducedProduct(
                    "productId1", "title1",
                    listOf(ColorSwatch("color1", "FF0000", "skuId1")),
                    "4.00", "price was 6.00 is now 4.00"
                )
            )
        )

        assertEquals(expectedResult, sourceProducts.toProducts (SourcePrice::mockPriceLabelGenerator))
    }


    val testJson = """{
    "products":[
        {
         "productId":"productId1",
         "title":"title1",
         "price":{
            "was":"1.00",
            "then1":"2.0",
            "then2":"3.0",
            "now":"4.0",
            "uom":"",
            "currency":"GBP"
         },"colorSwatches":[
            {
               "color":"color1",
               "basicColor":"basicColor1",
               "colorSwatchUrl":"colorSwatchUrl1",
               "imageUrl":"imageUrl1",
               "isAvailable":true,
               "skuId":"skuId1"
            }
         ]
      },
      {
         "productId":"productId2",
         "title":"title2",
         "price":{
            "was":{"from":"11.0","to":"12.0"},
            "then1":"13.0",
            "then2":"14.0",
            "now":{"from":"15.0","to":"16.0"},
            "uom":"",
            "currency":"USD"
         },"colorSwatches":[
            {
               "color":"color2",
               "basicColor":"basicColor2",
               "colorSwatchUrl":"colorSwatchUrl2",
               "imageUrl":"imageUrl2",
               "isAvailable":true,
               "skuId":"skuId2"
            }
         ]
      }
      ]}
""".trimIndent()
}