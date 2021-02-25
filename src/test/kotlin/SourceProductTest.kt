import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SourceProductTest {
    @Test
    fun `A StringOrFromTo is only equal to another StringOrFromTo when both have the same type and all values match`() {
        val nowString_10_0 = StringOrFromTo.String("10")
        val nowString_10_1 = StringOrFromTo.String("10")
        val nowString_60 = StringOrFromTo.String("60")
        assertTrue(nowString_10_0 == nowString_10_1)
        assertTrue(nowString_10_0 != nowString_60)

        val nowFromTo_10_20_0 = StringOrFromTo.FromTo("10","20")
        val nowFromTo_10_20_1 = StringOrFromTo.FromTo("10","20")
        val nowFromTo_15_20 = StringOrFromTo.FromTo("10","30")
        val nowFromTo_10_30 = StringOrFromTo.FromTo("10","30")
        assertTrue(nowFromTo_10_20_0 == nowFromTo_10_20_1)
        assertTrue(nowFromTo_10_20_0 != nowFromTo_15_20)
        assertTrue(nowFromTo_10_20_0 != nowFromTo_10_30)

        assertTrue((nowString_10_0 as StringOrFromTo) != (nowFromTo_10_20_0 as StringOrFromTo))
    }
    @Test
    fun `decoding product json results in a SourceProduct object containing the correct data`(){
        jacksonMapper.jacksonConfiguration()
        val result = jacksonMapper.readValue<SourceProducts>(testJson)

        val expectedResult = SourceProducts(
            listOf( SourceProduct("productId1","title1",
                                listOf(SourceColorSwatch("color1","basicColor1","skuId1")),
                                SourcePrice(StringOrFromTo.String("1.00"),"2.0","3.0",StringOrFromTo.String("4.0"),"GBP")),
                    SourceProduct("productId2","title2",
                                listOf(SourceColorSwatch("color2","basicColor2","skuId2")),
                                SourcePrice(StringOrFromTo.FromTo("11.0","12.0"),"13.0","14.0",StringOrFromTo.FromTo("15.0","16.0"),"USD"))
            )
        )

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