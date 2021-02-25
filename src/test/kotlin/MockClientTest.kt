import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineExceptionHandler
import org.hamcrest.CoreMatchers
import org.junit.Assert
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.IllegalStateException
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MockClientTest {
    @Test
    fun `mock client should return OK if url and method match expected url and method`() = runBlocking{

        val mockClient = createMockClient("https://mike.com", HttpMethod.Get,"success")
        val result = mockClient.get<String>("https://mike.com")
        assertEquals("success",result)
    }

    @Test
    fun `mock client throws an exception if method or url doesnt match the expected url or method`() = runBlocking {
        val mockClient = createMockClient("https://mike.com", HttpMethod.Get,"success")

        try {
            mockClient.get<String>("https://wrongurl")
            assertTrue(false) //should always throw
        } catch(e:Throwable) {
            assertTrue(e is IllegalStateException)
            assertEquals("Client request(https://wrongurl/) invalid: 400 Bad Request",(e as IllegalStateException).message )
        }
    }
}