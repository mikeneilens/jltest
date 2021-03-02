package config

import org.junit.jupiter.api.Test
import toRgbColor
import kotlin.test.assertEquals

class ColorMapTest {
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