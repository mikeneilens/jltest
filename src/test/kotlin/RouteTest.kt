import io.ktor.config.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class RouteTest  {
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

    val url = "https://api.johnlewis.com/search/api/rest/v2/catalog/products/search/keyword?q=dresses"
    val key = "key=abcd"
    val client = createMockClient("$url&$key", HttpMethod.Get, testJson)

    @Test
    fun `getting products returns a list of products`() = withTestApplication( {
        (environment.config as MapApplicationConfig).apply {
            put("ktor.environment", "local")
            put("services.local.jl.url", url)
            put("services.local.jl.key", key)
        }
        mainModule(client)
    } ){
        val call = handleRequest(HttpMethod.Get, "/products")
        val expectedResult = """
            {
              "products" : [ {
                "productId" : "productId1",
                "title" : "title1",
                "colorSwatches" : [ {
                  "color" : "color1",
                  "rgbColor" : "unknown",
                  "skuId" : "skuId1"
                } ],
                "nowPrice" : "4.0",
                "priceLabel" : "Was £1.00, now £4.0"
              }, {
                "productId" : "productId2",
                "title" : "title2",
                "colorSwatches" : [ {
                  "color" : "color2",
                  "rgbColor" : "unknown",
                  "skuId" : "skuId2"
                } ],
                "nowPrice" : "15.0",
                "priceLabel" : "Was $11.0 - 12.0, now $15.0 - 16.0"
              } ]
            }
        """.trimIndent()

        assertEquals(HttpStatusCode.OK, call.response.status())
        assertEquals(expectedResult, call.response.content)
    }


}