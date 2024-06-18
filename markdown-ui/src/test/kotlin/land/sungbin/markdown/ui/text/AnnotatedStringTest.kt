package land.sungbin.markdown.ui.text

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.test.Test
import land.sungbin.markdown.runtime.MarkdownOptions
import land.sungbin.markdown.ui.text.AnnotatedString.TextAndStyle
import okio.buffer

class AnnotatedStringTest {
  @Test fun annotated() {
    val texts = listOf(
      TextAndStyle("안녕하세요", TextStyle(underline = true)),
      TextAndStyle(" "),
      TextAndStyle("잘가세요", TextStyle(strikethrough = true, bold = true)),
    )
    val annotated = AnnotatedString(texts)

    assertThat(annotated.text(MarkdownOptions.Default).buffer().readUtf8())
      .isEqualTo("<ins>안녕하세요</ins> ~~**잘가세요**~~")
  }
}