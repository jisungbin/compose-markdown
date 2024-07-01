/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/compose-markdown/blob/main/LICENSE
 */

package land.sungbin.markdown.ui.modifier

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.test.Test
import land.sungbin.markdown.ui.transform

class FootnoteTransformerTest {
  @Test fun footnotePositionStartsWithZero() {
    val result = FootnoteModifier(tag = "bye", position = { 0 }).transform("hello, world!")
    assertThat(result).isEqualTo("[^bye]hello, world!")
  }

  @Test fun footnotePositionInMiddle() {
    val result = FootnoteModifier(tag = "bye", position = { 5 }).transform("hello, world!")
    assertThat(result).isEqualTo("hello,[^bye] world!")
  }

  @Test fun footnotePositionEndsWithLastIndex() {
    val result = FootnoteModifier(tag = "bye", position = { 12 }).transform("hello, world!")
    assertThat(result).isEqualTo("hello, world![^bye]")
  }
}
