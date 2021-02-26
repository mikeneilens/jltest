import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class PriceFormatterTest {
    @Test
    fun `price which isn't a number is returned unchanged`() {
        assertEquals("a2.34", "a2.34".priceFormatter())
    }
    @Test
    fun `price with decimals below ten is converted to a price with decimals`() {
        assertEquals("2.34", "2.34".priceFormatter())
    }
    @Test
    fun `price with decimals bigger or equal to ten is converted to a price with decimals`() {
        assertEquals("10.34", "10.34".priceFormatter())
    }
    @Test
    fun `price with 00 decimals bigger or equal to ten is converted to a price with no decimals`() {
        assertEquals("10", "10.00".priceFormatter())
    }
    @Test
    fun `price with no decimals below ten is converted to a price with decimals`() {
        assertEquals("2.00", "2".priceFormatter())
    }
    @Test
    fun `price with no decimals bigger or equal to ten is converted to a price with no decimals`() {
        assertEquals("11", "11".priceFormatter())
    }

}
