package land.sungbin.markdown.ui.quote

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.test.Test
import land.sungbin.markdown.ui.bufferOf

class QuoteTest {
  @Test fun quote() {
    val quote = QuoteText(bufferOf("안녕 bye"))
    assertThat(quote.source { readUtf8() }).isEqualTo("> 안녕 bye")
  }
}
