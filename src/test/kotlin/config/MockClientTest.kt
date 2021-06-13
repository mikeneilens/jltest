package config

import createMockClient
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*

class MockClientTest:StringSpec( {
    "when url and method match expected url and method, mock client should return OK"{
        val mockClient = createMockClient("https://mike.com", HttpMethod.Get, "success")
        mockClient.get<String>("https://mike.com") shouldBe "success"
    }
    "when url and method match expected url and method but response is expected to be 401, mock client should return 401" {
        val errorMessaage = """
            {
             "code": 3,
             "message": "API key not valid. Please pass a valid API key.",
             "details": [
              {
               "@type": "type.googleapis.com/google.rpc.DebugInfo",
               "stackEntries": [],
               "detail": "service_control"
              }
             ]
            }
        """.trimIndent()

        val mockClient = createMockClient("https://mike.com", HttpMethod.Get, errorMessaage, HttpStatusCode.Forbidden)
        val result = mockClient.get<HttpResponse>("https://mike.com")

        result.status shouldBe HttpStatusCode.Forbidden
    }

})