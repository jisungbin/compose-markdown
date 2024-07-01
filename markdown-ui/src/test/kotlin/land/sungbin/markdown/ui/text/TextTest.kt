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

class TextTest {
  @Test fun singleLineText(): Unit = runTest {
    val result = markdown { Text("Hello, World!") }
    assertThat(result).isEqualTo("Hello, World!")
  }

  @Test fun multiLineText(): Unit = runTest {
    val result = markdown {
      Text("Hello,")
      Text("World!")
    }
    assertThat(result).isEqualTo("Hello,\nWorld!")
  }

  @Test fun textWithStyle(): Unit = runTest {
    val result = markdown {
      Text(
        buildAnnotatedString {
          withStyle(TextStyle(bold = true)) { "Hello" }
          append(", ")
          withStyle(TextStyle(italics = true)) { "World" }
        },
      )
    }
    assertThat(result).isEqualTo("**Hello**, _World_")
  }

  @Test fun emptyTextForNewLine(): Unit = runTest {
    val result = markdown {
      Text("Hello,")
      Text("")
      Text("World!")
    }
    assertThat(result).isEqualTo("Hello,\n\nWorld!")
  }
}
