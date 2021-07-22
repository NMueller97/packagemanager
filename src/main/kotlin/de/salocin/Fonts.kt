package de.salocin

import org.controlsfx.glyphfont.FontAwesome
import org.controlsfx.glyphfont.GlyphFontRegistry

object Fonts {

    val fontAwesome = GlyphFontRegistry.font("FontAwesome")

    fun FontAwesome.Glyph.create() = fontAwesome.create(this)
}
