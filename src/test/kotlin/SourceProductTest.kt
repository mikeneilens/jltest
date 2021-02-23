import com.fasterxml.jackson.module.kotlin.readValue
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

val json = """{
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

class SourceProductTest {
    @Test
    fun `decoding product json results in a SourceProduct object containing the correct data`(){
        jacksonMapper.jacksonConfiguration()
        val result = jacksonMapper.readValue<SourceProducts>(json)
        assertEquals(2, result.products.size)

        val product1 = result.products[0]
        val product2 = result.products[1]
        assertEquals("productId1", product1.productId)
        assertEquals("productId2", product2.productId)
        assertEquals("title1", product1.title)
        assertEquals("title2", product2.title)

        assertTrue(product1.price.was is StringOrFromTo.String)
        assertEquals( "1.00", (product1.price.was as StringOrFromTo.String).value)

        assertTrue(product2.price.was is StringOrFromTo.FromTo)
        assertEquals( "11.0", (product2.price.was as StringOrFromTo.FromTo).from)
        assertEquals( "12.0", (product2.price.was as StringOrFromTo.FromTo).to)

        assertEquals( "2.0", product1.price.then1)
        assertEquals( "13.0", product2.price.then1)

        assertEquals( "3.0", product1.price.then2)
        assertEquals( "14.0", product2.price.then2)

        assertTrue(product1.price.now is StringOrFromTo.String)
        assertEquals( "4.0", (product1.price.now as StringOrFromTo.String).value)

        assertTrue(product2.price.now is StringOrFromTo.FromTo)
        assertEquals( "15.0", (product2.price.now as StringOrFromTo.FromTo).from)
        assertEquals( "16.0", (product2.price.now as StringOrFromTo.FromTo).to)

        assertEquals( "GBP", product1.price.currency)
        assertEquals( "USD", product2.price.currency)

        assertEquals( 1, product1.colorSwatches.size)
        assertEquals( 1, product2.colorSwatches.size)

        assertEquals( "color1", product1.colorSwatches[0].color)
        assertEquals( "color2", product2.colorSwatches[0].color)
        assertEquals( "basicColor1", product1.colorSwatches[0].basicColor)
        assertEquals( "basicColor2", product2.colorSwatches[0].basicColor)
        assertEquals( "skuId1", product1.colorSwatches[0].skuId)
        assertEquals( "skuId2", product2.colorSwatches[0].skuId)
    }
}