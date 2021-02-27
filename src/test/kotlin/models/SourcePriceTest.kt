package models

import StringOrFromTo
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class SourcePriceTest {
    @Test
    fun `A StringOrFromTo is only equal to another StringOrFromTo when both have the same type and all values match`() {
        val nowString_10_0 = StringOrFromTo.String("10")
        val nowString_10_1 = StringOrFromTo.String("10")
        val nowString_60 = StringOrFromTo.String("60")
        assertTrue(nowString_10_0 == nowString_10_1)
        assertTrue(nowString_10_0 != nowString_60)

        val nowFromTo_10_20_0 = StringOrFromTo.FromTo("10", "20")
        val nowFromTo_10_20_1 = StringOrFromTo.FromTo("10", "20")
        val nowFromTo_15_20 = StringOrFromTo.FromTo("10", "30")
        val nowFromTo_10_30 = StringOrFromTo.FromTo("10", "30")
        assertTrue(nowFromTo_10_20_0 == nowFromTo_10_20_1)
        assertTrue(nowFromTo_10_20_0 != nowFromTo_15_20)
        assertTrue(nowFromTo_10_20_0 != nowFromTo_10_30)

        assertTrue((nowString_10_0 as StringOrFromTo) != (nowFromTo_10_20_0 as StringOrFromTo))

        val emptyString = StringOrFromTo.String("")
        val empty = StringOrFromTo.Empty
        assertTrue((emptyString as StringOrFromTo) != (empty as StringOrFromTo))
    }
}