package config

import createMockClient
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class MockClientTest {
    @Test
    fun `mock client should return OK if url and method match expected url and method`() = runBlocking{

        val mockClient = createMockClient("https://mike.com", HttpMethod.Get, "success")
        val result = mockClient.get<String>("https://mike.com")
        assertEquals("success",result)
    }

    @Test
    fun `mock client should return 401 if url and method match expected url and method but response is expected to be 401`() = runBlocking{

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
        assertEquals(HttpStatusCode.Forbidden, result.status)
    }

    @Test
    fun `mock client throws an exception if method or url doesnt match the expected url or method`() = runBlocking {
        val mockClient = createMockClient("https://mike.com", HttpMethod.Get, "success")
        val result:HttpResponse = mockClient.get("https://wrongurl")
        assertEquals(HttpStatusCode.BadRequest, result.status)
    }
}