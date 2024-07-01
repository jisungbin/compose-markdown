/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/compose-markdown/blob/main/LICENSE
 */

package land.sungbin.markdown.ui.list

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.test.Ignore
import kotlin.test.Test
import kotlinx.coroutines.test.runTest
import land.sungbin.markdown.runtime.markdown
import land.sungbin.markdown.ui.text.Text

class ListTest {
  @Test fun singleOrderedList(): Unit = runTest {
    val result = markdown {
      List(ordered = true) {
        Text("One")
        Text("Two")
        Text("Three\nFour")
      }
    }
    assertThat(result).isEqualTo(
      """
      1. One
      2. Two
      3. Three
         Four
      """.trimIndent(),
    )
  }

  @Test fun singleUnorderedList(): Unit = runTest {
    val result = markdown {
      List {
        Text("One")
        Text("Two")
        Text("Three")
      }
    }
    assertThat(result).isEqualTo(
      """
      - One
      - Two
      - Three
      """.trimIndent(),
    )
  }

  @Ignore("TODO")
  @Test fun nestedOrderedList(): Unit = runTest {
    val result = markdown {
      List(ordered = true) {
        Text("One")
        List(ordered = true) {
          Text("Two")
          Text("Three")
          List(ordered = true) {
            Text("Four")
            Text("Five")
          }
        }
        Text("Six")
        Text("Seven")
      }
    }
    assertThat(result).isEqualTo(
      """
      1. One
         1. Two
         2. Three
            1. Four
            2. Five
      2. Six
      3. Seven
      """.trimIndent(),
    )
  }
}
