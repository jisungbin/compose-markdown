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

class FootnoteModifierTest {
  @Test fun singleLineFootnote(): Unit = runTest {
    val result = markdown {
      Text(
        "Hello, World!",
        modifier = Modifier.footnote("R", { 4 }) {
          Text("BYE!")
        },
      )
    }
    assertThat(result).isEqualTo(
      """
        Hello[^R], World!

        [^R]: BYE!
        
      """.trimIndent(),
    )
  }

  @Test fun multiLineFootnote(): Unit = runTest {
    val result = markdown {
      Text(
        "Hello, World!",
        modifier = Modifier.footnote("R", { 4 }) {
          Text("BYE!")
          Text("BYE!")
        },
      )
    }
    assertThat(result).isEqualTo(
      """
        Hello[^R], World!

        [^R]: BYE!
            BYE!
        
      """.trimIndent(),
    )
  }

  @Test fun mixedFootnote(): Unit = runTest {
    val result = markdown {
      Text("No Footnote!")
      Text(
        "Hello, World!",
        modifier = Modifier.footnote("R", { 4 }) {
          Text("BYE!")
        },
      )
      Text(
        "Hello, World!",
        modifier = Modifier.footnote("R2", { 4 }) {
          Text("BYE!")
          Text("BYE!")
        },
      )
      Text("No Footnote!")
    }
    assertThat(result).isEqualTo(
      """
        No Footnote!
        Hello[^R], World!
        Hello[^R2], World!
        No Footnote!
        
        [^R]: BYE!
        
        [^R2]: BYE!
            BYE!
        
      """.trimIndent(),
    )
  }
}
