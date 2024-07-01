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

class ClickableTransformerTest {
  @Test fun clickableRangeStartsWithZero() {
    val result = ClickableTransformer(link = "bye", range = { 0..4 }).transform("hello, world!")
    assertThat(result).isEqualTo("[hello](bye), world!")
  }

  @Test fun clickableRangeInMiddle() {
    val result = ClickableTransformer(link = "bye", range = { 5..6 }).transform("hello, world!")
    assertThat(result).isEqualTo("hello[, ](bye)world!")
  }

  @Test fun clickableRangeEndsWithLastIndex() {
    val result = ClickableTransformer(link = "bye", range = { 7..12 }).transform("hello, world!")
    assertThat(result).isEqualTo("hello, [world!](bye)")
  }
}
