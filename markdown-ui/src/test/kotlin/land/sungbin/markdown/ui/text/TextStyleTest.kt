/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/compose-markdown/blob/main/LICENSE
 */

package land.sungbin.markdown.ui.text

import assertk.assertFailure
import assertk.assertThat
import assertk.assertions.hasMessage
import assertk.assertions.isEqualTo
import kotlin.test.Test
import land.sungbin.markdown.ui.transform

class TextStyleTest {
  @Test fun multipleUppercaseStyled() {
    val styled = TextStyle(
      bold = true,
      italics = true,
      strikethrough = true,
      underline = true,
      monospace = true,
      uppercase = true,
    ).transform("안녕하세요 hello~")

    assertThat(styled.buffer.readUtf8()).isEqualTo("`<ins>~~_**안녕하세요 HELLO~**_~~</ins>`")
  }

  @Test fun cannotUppercaseAndLowercaseAtTheSameTime() {
    val failure = assertFailure { TextStyle(uppercase = true, lowercase = true) }
    failure.hasMessage("Cannot set uppercase and lowercase at the same time")
  }

  @Test fun lowercaseStyled() {
    val styled = TextStyle(lowercase = true).transform("HELLO")
    assertThat(styled.buffer.readUtf8()).isEqualTo("hello")
  }
}
