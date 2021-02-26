import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ReturnedProductTest {
    @Test
    fun `converting a string containing a colour to an rgbColor gives the correct hex value for the color`() {
        assertEquals("000000", "Black".toRgbColor())
        assertEquals("8B0000", "DarkRed".toRgbColor())
        assertEquals("A52A2A","Brown".toRgbColor())
    }
    @Test
    fun `converting a string containing an unknown color gives unkown as the rgbColor`() {
        assertEquals("unknown", "stripey".toRgbColor())
    }

}