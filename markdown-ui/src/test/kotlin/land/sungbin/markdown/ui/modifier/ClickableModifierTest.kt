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
import kotlinx.coroutines.test.runTest
import land.sungbin.markdown.runtime.markdown
import land.sungbin.markdown.ui.text.Text

class ClickableModifierTest {
  @Test fun clickable(): Unit = runTest {
    val result = markdown {
      Text(
        "Hello, World!",
        modifier = Modifier.clickable("https://example.com") { 3..5 },
      )
    }
    assertThat(result).isEqualTo("Hel[lo,](https://example.com) World!")
  }
}
