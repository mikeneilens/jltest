import io.ktor.config.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ProductApiConnectorTest {
    val config = MapApplicationConfig(
        "ktor.environment" to "local",
        "services.local.jl.url" to "https://api.johnlewis.com/search/api/rest/v2/catalog/products/search/keyword?q=dresses",
        "services.local.jl.key" to "key=abcd"
    )
    @Test
    fun `apiConnector can return a valid SourceProduct `() = runBlocking{

        val client = createMockClient("https://api.johnlewis.com/search/api/rest/v2/catalog/products/search/keyword?q=dresses&key=abcd", HttpMethod.Get, testJson)
        val productApiConnector = ProductApiConnector(config,client)
        val expectedResult = SourceProducts(
            listOf( SourceProduct("productId1","title1",
                                        listOf(SourceColorSwatch("color1","basicColor1","skuId1")),
                                        SourcePrice(PriceType.Single(1.00),PriceType.Single(2.0),PriceType.Single(3.0),PriceType.Single(4.0),"GBP")),
                    SourceProduct("productId2","title2",
                                        listOf(SourceColorSwatch("color2","basicColor2","skuId2")),
                                        SourcePrice(PriceType.FromTo(11.0,12.0),PriceType.Single(13.0),PriceType.Single(14.0),PriceType.FromTo(15.0,16.0),"USD"))
            )
        )

        val result = productApiConnector.getProducts()
        assertEquals(2, result.products.size)
        assertEquals(expectedResult, result)
    }

    @Test
    fun `apiConnector returns BadRequest if the request uses an invalid key`()  = runBlocking{
        val client = createMockClient(
            "https://api.johnlewis.com/search/api/rest/v2/catalog/products/search/keyword?q=dresses&key=abcd",
            HttpMethod.Get, testJson,
            HttpStatusCode.Forbidden)
        val productApiConnector = ProductApiConnector(config,client)
        try {
            val result = productApiConnector.getProducts()
            assertFalse(true,"Expected error to be thrown")
        } catch(e:Throwable) {
            assertTrue(e is BadRequestException)
        }
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



