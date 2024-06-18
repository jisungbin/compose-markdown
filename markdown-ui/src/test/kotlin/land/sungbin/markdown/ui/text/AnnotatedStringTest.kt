package land.sungbin.markdown.ui.text

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.test.Test
import land.sungbin.markdown.ui.text.AnnotatedString.TextAndStyle

class AnnotatedStringTest {
  @Test fun annotated() {
    val texts = listOf(
      TextAndStyle("안녕하세요", TextStyle(underline = true)),
      TextAndStyle(" "),
      TextAndStyle("잘가세요", TextStyle(strikethrough = true, bold = true)),
    )
    val annotated = AnnotatedString(texts)

    assertThat(annotated.source { readUtf8() })
      .isEqualTo("<ins>안녕하세요</ins> ~~**잘가세요**~~")
  }

  @Test fun annotatedBuilder() {
    val annotated = buildAnnotatedString {
      append("normal")
      append(" ")
      withStyle(TextStyle(bold = true)) { "bold" }
      append(" ")
      withStyle(TextStyle(italics = true)) { "italics" }
      append(" ")
      withStyle(TextStyle(strikethrough = true)) { "strikethrough" }
      append(" ")
      withStyle(TextStyle(underline = true)) { "underline" }
      append(" ")
      withStyle(TextStyle(monospace = true)) { "monospace" }
      append(" ")
      withStyle(TextStyle(lowercase = true)) { "LOWERCASE" }
      append(" ")
      withStyle(TextStyle(uppercase = true)) { "uppercase" }
      append(" ")
      withStyle(
        TextStyle(
          bold = true,
          italics = true,
          strikethrough = true,
          underline = true,
          monospace = true,
          uppercase = true,
        ),
      ) {
        "multipleUppercaseStyled"
      }
    }
    assertThat(annotated.source { readUtf8() }).isEqualTo(
      "" +
        "normal " +
        "**bold** " +
        "_italics_ " +
        "~~strikethrough~~ " +
        "<ins>underline</ins> " +
        "`monospace` " +
        "lowercase " +
        "UPPERCASE " +
        "`<ins>~~_**MULTIPLEUPPERCASESTYLED**_~~</ins>`",
    )
  }
}