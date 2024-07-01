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
import land.sungbin.markdown.ui.transform

class TextStyleDefinitionTest {
  @Test fun bold() {
    val styled = TextStyleDefinition.Bold.transform("HELLO hello~")
    assertThat(styled).isEqualTo("**HELLO hello~**")
  }

  @Test fun italic() {
    val styled = TextStyleDefinition.Italic.transform("HELLO hello~")
    assertThat(styled).isEqualTo("_HELLO hello~_")
  }

  @Test fun strikethrough() {
    val styled = TextStyleDefinition.Strikethrough.transform("HELLO hello~")
    assertThat(styled).isEqualTo("~~HELLO hello~~~")
  }

  @Test fun underline() {
    val styled = TextStyleDefinition.Unerline.transform("HELLO hello~")
    assertThat(styled).isEqualTo("<ins>HELLO hello~</ins>")
  }

  @Test fun monospace() {
    val styled = TextStyleDefinition.Monospace.transform("HELLO hello~")
    assertThat(styled).isEqualTo("`HELLO hello~`")
  }
}
