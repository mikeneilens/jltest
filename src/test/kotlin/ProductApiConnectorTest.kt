import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.ktor.config.*
import io.ktor.http.*

class ProductApiConnectorTest: StringSpec({
    val testJson = """{
    "products":[
        {
         "productId":"productId1",
         "title":"title1",
         "price":{
            "was":"",
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

    "ProductAPI Connector creates a list of products when the request is valid" {
        val url = "https://api.johnlewis.com/search/api/rest/v2/catalog/products/search/keyword?q=dresses"
        val key = "key=abcd"
        val client = createMockClient("$url&$key", HttpMethod.Get, testJson)

        val config = MapApplicationConfig(
            "ktor.environment" to "local",
            "services.local.jl.url" to url,
            "services.local.jl.key" to key
        )
        val sourceProducts = ProductApiConnector(config,client).getProducts()
        sourceProducts.products.size shouldBe 2

    }
    "ProdutAPI Connector throws an exception if the request returns a 500" {
        val url = "https://api.johnlewis.com/search/api/rest/v2/catalog/products/search/keyword?q=dresses"
        val key = "key=abcd"
        val client = createMockClient("$url&$key", HttpMethod.Get, testJson, HttpStatusCode.InternalServerError)

        val config = MapApplicationConfig(
            "ktor.environment" to "local",
            "services.local.jl.url" to url,
            "services.local.jl.key" to key
        )

        val runTimeException = shouldThrow<RuntimeException> {
            ProductApiConnector(config,client).getProducts()
        }
        runTimeException.message shouldBe "400 Bad Request: ErrorResponse(code=n/a, field=n/a)"
    }
    "ProdutAPI Connector throws an exception if the server returns json that won't parse into a product" {
        val incorrectJson = """
            {"message":"this is not a product!"}
        """.trimIndent()
        val url = "https://api.johnlewis.com/search/api/rest/v2/catalog/products/search/keyword?q=dresses"
        val key = "key=abcd"
        val client = createMockClient("$url&$key", HttpMethod.Get, incorrectJson)

        val config = MapApplicationConfig(
            "ktor.environment" to "local",
            "services.local.jl.url" to url,
            "services.local.jl.key" to key
        )

        val runTimeException = shouldThrow<RuntimeException> {
            ProductApiConnector(config,client).getProducts()
        }
        runTimeException.message shouldBe "400 Bad Request: ErrorResponse(code=n/a, field=n/a)"
    }
})