import com.fasterxml.jackson.module.kotlin.readValue
import config.jacksonConfiguration
import io.kotest.core.spec.style.WordSpec
import io.kotest.matchers.shouldBe

class SourceProductTest: WordSpec( {
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

    "decoding product json" should {
        "result in a SourceProduct Object containing the correct data" {
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
            result shouldBe expectedResult
        }
    }

    "converting a SourceProduct to a ReduceProduct" should {

        "create a ReducedProduct containing a single price when the original product contains a single price" {
            fun SourcePrice.mockPriceLabelGenerator() = "price is $now"

            val sourceProduct = SourceProduct(
                productId ="productId1",
                title = "title1",
                colorSwatches = listOf(SourceColorSwatch("color1", "Red", "skuId1")),
                price = SourcePrice(PriceType.Single(1.00), PriceType.Single(2.0), PriceType.Single(3.0), PriceType.Single(4.0), "GBP")
            )

            val expectedResult = ReducedProduct(
                productId ="productId1",
                title = "title1",
                colorSwatches =  listOf(ColorSwatch("color1", "FF0000", "skuId1")),
                nowPrice = "4.00",
                priceLabel = "price is 4.00"
            )
            sourceProduct.toProduct (SourcePrice::mockPriceLabelGenerator) shouldBe expectedResult
        }

        "create a ReducedProduct containing a from/to price when the original product contains a from/to price" {
            fun SourcePrice.mockPriceLabelGenerator() = "price is $now"

            val sourceProduct = SourceProduct(
                productId ="productId1",
                title = "title1",
                colorSwatches = listOf(SourceColorSwatch("color1", "Red", "skuId1")),
                price = SourcePrice(PriceType.Single(1.0), PriceType.Single(2.0), PriceType.Single(3.0), PriceType.FromTo(2.0, 3.0), "GBP")
            )

            val expectedResult = ReducedProduct(
                productId ="productId1",
                title = "title1",
                colorSwatches =  listOf(ColorSwatch("color1", "FF0000", "skuId1")),
                nowPrice = "2.00 - 3.00",
                priceLabel = "price is 2.00 - 3.00"
            )
            sourceProduct.toProduct (SourcePrice::mockPriceLabelGenerator) shouldBe expectedResult
        }

        "create a ReducedProduct containing an invalid price when the original product contains an invalid price" {
            fun SourcePrice.mockPriceLabelGenerator() = "price is $now"
            val sourceProduct = SourceProduct(
                productId = "productId1",
                title = "title1",
                colorSwatches = listOf(SourceColorSwatch("color1", "Red", "skuId1")),
                price = SourcePrice(PriceType.Single(1.0), PriceType.Single(2.0), PriceType.Single(3.0), PriceType.Invalid("two"), "GBP")
            )

            val expectedResult = ReducedProduct(
                productId = "productId1",
                title = "title1",
                colorSwatches = listOf(ColorSwatch("color1", "FF0000", "skuId1")),
                nowPrice = "two",
                priceLabel = "price is two"
            )
            sourceProduct.toProduct(SourcePrice::mockPriceLabelGenerator) shouldBe expectedResult
        }
    }

    "converting SourceProdocuts to ReducedProducts" should {
        "create an object one ReducedProduct when the SourceProducts contains two products with only one product discuounted"{
            fun SourcePrice.mockPriceLabelGenerator() = "price was $was is now $now"
            val sourceProducts = SourceProducts(
                listOf(
                    SourceProduct(
                        productId = "productId1",
                        title = "title1 reduced",
                        colorSwatches = listOf(SourceColorSwatch("color1", "Red", "skuId1")),
                        price = SourcePrice(PriceType.Single(6.0), PriceType.Single(5.0), PriceType.Single(4.50), PriceType.Single(4.0), "GBP")
                    ),
                    SourceProduct(
                        productId = "productId2",
                        title = "title2",
                        colorSwatches = listOf(SourceColorSwatch("color2", "Blue", "skuId1")),
                        price = SourcePrice(PriceType.Empty, PriceType.Single(2.0), PriceType.Single(3.0), PriceType.Single(4.5), "GBP")
                    )
                )
            )

            val expectedResult = ReducedProducts(
                listOf(
                    ReducedProduct(
                        productId = "productId1",
                        title = "title1 reduced",
                        colorSwatches = listOf(ColorSwatch("color1", "FF0000", "skuId1")),
                        nowPrice = "4.00",
                        priceLabel = "price was 6.00 is now 4.00"
                    )
                )
            )
            sourceProducts.toProducts (SourcePrice::mockPriceLabelGenerator) shouldBe expectedResult
        }
    }

})