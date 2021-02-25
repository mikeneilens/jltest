
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.config.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ProductApiConnectorTest {

    @Test
    fun `apiConnector can return a valid SourceProduct `() = runBlocking{
        val config = MapApplicationConfig(
            "ktor.environment" to "local",
            "services.local.jl.url" to "https://api.johnlewis.com/search/api/rest/v2/catalog/products/search/keyword?q=dresses",
            "services.local.jl.key" to "key=abcd"
        )
        val client = createMockClient("https://api.johnlewis.com/search/api/rest/v2/catalog/products/search/keyword?q=dresses&key=abcd", HttpMethod.Get, testJson)
        val productApiConnector = ProductApiConnector(config,client)
        val expectedResult = SourceProducts(
            listOf( SourceProduct("productId1","title1",
                                        listOf(SourceColorSwatch("color1","basicColor1","skuId1")),
                                        SourcePrice(StringOrFromTo.String("1.00"),"2.0","3.0",StringOrFromTo.String("4.0"),"GBP")),
                    SourceProduct("productId2","title2",
                                        listOf(SourceColorSwatch("color2","basicColor2","skuId2")),
                                        SourcePrice(StringOrFromTo.FromTo("11.0","12.0"),"13.0","14.0",StringOrFromTo.FromTo("15.0","16.0"),"USD"))
            )
        )

        val result = productApiConnector.getProducts()
        assertEquals(2, result.products.size)
        assertEquals(expectedResult, result)
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



