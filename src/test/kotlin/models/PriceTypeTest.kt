package models

import PriceType
import org.junit.jupiter.api.Test
import kotlin.test.assertTrue

class PriceTypeTest {
    @Test
    fun `A PriceType is only equal to another PriceType when both have the same type and all values match`() {
        val nowString_10_0 = PriceType.Single(10.0)
        val nowString_10_1 = PriceType.Single(10.0)
        val nowString_60 = PriceType.Single(60.0)
        assertTrue(nowString_10_0 == nowString_10_1)
        assertTrue(nowString_10_0 != nowString_60)

        val nowFromTo_10_20_0 = PriceType.FromTo(10.0, 20.0)
        val nowFromTo_10_20_1 = PriceType.FromTo(10.0, 20.0)
        val nowFromTo_15_20 = PriceType.FromTo(10.0, 30.0)
        val nowFromTo_10_30 = PriceType.FromTo(10.0, 30.0)
        assertTrue(nowFromTo_10_20_0 == nowFromTo_10_20_1)
        assertTrue(nowFromTo_10_20_0 != nowFromTo_15_20)
        assertTrue(nowFromTo_10_20_0 != nowFromTo_10_30)

        assertTrue((nowString_10_0 as PriceType) != (nowFromTo_10_20_0 as PriceType))

        val emptyString = PriceType.Single(0.0)
        val empty = PriceType.Empty
        val empty_1 = PriceType.Empty
        assertTrue((empty as PriceType) == (empty_1 as PriceType))
        assertTrue((emptyString as PriceType) != (empty as PriceType))

        val invalid1 = PriceType.Invalid("abc")
        val invalid2 = PriceType.Invalid("abc")
        val invalid3 = PriceType. Invalid("ab")
        assertTrue((invalid1 as PriceType) == (invalid2 as PriceType))
        assertTrue((invalid1 as PriceType) != (invalid3 as PriceType))


    }
}