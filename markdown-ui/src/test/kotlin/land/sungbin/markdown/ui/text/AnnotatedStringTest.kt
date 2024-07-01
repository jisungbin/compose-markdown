/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/compose-markdown/blob/main/LICENSE
 */

package land.sungbin.markdown.ui.text

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.test.Test
import land.sungbin.markdown.ui.text.AnnotatedString.TextAndStyle

class AnnotatedStringTest {
  @Test fun annotated() {
    val texts = listOf(
      TextAndStyle("Hello", TextStyle(underline = true)),
      TextAndStyle(" "),
      TextAndStyle("Bye", TextStyle(strikethrough = true, bold = true)),
    )
    val annotated = AnnotatedString(texts)

    assertThat(annotated).isEqualTo("<ins>Hello</ins> ~~**Bye**~~")
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
    assertThat(annotated).isEqualTo(
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
