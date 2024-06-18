package land.sungbin.markdown.ui.quote

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.test.Test
import land.sungbin.markdown.ui.bufferOf
import land.sungbin.markdown.ui.text.TextStyle
import land.sungbin.markdown.ui.text.buildAnnotatedString

class QuoteTest {
  @Test fun plainQuote() {
    val quote = QuoteText(bufferOf("안녕 bye"))
    assertThat(quote.source { readUtf8() }).isEqualTo("> 안녕 bye")
  }

  @Test fun styeldQuote() {
    val text = buildAnnotatedString {
      append("안녕하세요 ")
      withStyle(TextStyle(bold = true)) { "My" }
      append(" ")
      withStyle(TextStyle(underline = true)) { "name" }
      append(" is ")
      withStyle(TextStyle(italics = true, strikethrough = true)) { "Ghost" }
      append(".")
    }
    val quote = QuoteText(bufferOf(text))

    assertThat(quote.source { readUtf8() })
      .isEqualTo("> 안녕하세요 **My** <ins>name</ins> is ~~_Ghost_~~.")
  }
}
