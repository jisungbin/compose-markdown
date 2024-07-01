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
import kotlinx.coroutines.test.runTest
import land.sungbin.markdown.runtime.markdown

class CodeBlockTest {
  @Test fun noLanguage(): Unit = runTest {
    val result = markdown {
      Code {
        Text("fun main() {")
        Text("  println(\"Hello, World!\")")
        Text("}")
      }
    }
    assertThat(result).isEqualTo(
      """
      |```
      |fun main() {
      |  println("Hello, World!")
      |}
      |```
      """.trimMargin(),
    )
  }

  @Test fun withLanguage(): Unit = runTest {
    val result = markdown {
      Code(language = "kotlin") {
        Text("fun main() {")
        Text("  println(\"Hello, World!\")")
        Text("}")
      }
    }
    assertThat(result).isEqualTo(
      """
      |```kotlin
      |fun main() {
      |  println("Hello, World!")
      |}
      |```
      """.trimMargin(),
    )
  }
}
