import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class LabelTypeTest {
    @Test
    fun `query parms map to the correct labelType`() {
        assertEquals(LabelType.ShowWasNow, "ShowWasNow".toLabelType())
        assertEquals(LabelType.ShowWasThenNow,"ShowWasThenNow".toLabelType())
        assertEquals(LabelType.ShowPercDiscount,"ShowPercDiscount".toLabelType())
        val nullString:String? = null
        assertEquals(LabelType.None, nullString.toLabelType())
    }
}