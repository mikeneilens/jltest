package config

import io.kotest.core.spec.style.StringSpec
import io.kotest.data.row
import io.kotest.matchers.shouldBe
import toRgbColor

class ColorMapTest : StringSpec( {
    "converting a string containing a colour to an rgbColor gives the correct hex value for the color" {
        listOf(
            row(    "Black",    "000000"),
            row(    "DarkRed",  "8B0000"),
            row(    "Brown",    "A52A2A")
        ).forEach { (color,rgbColor) ->
            color.toRgbColor() shouldBe rgbColor
        }
    }
})