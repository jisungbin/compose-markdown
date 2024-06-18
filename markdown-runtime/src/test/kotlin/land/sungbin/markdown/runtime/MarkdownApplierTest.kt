/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/compose-markdown/blob/main/LICENSE
 */

package land.sungbin.markdown.runtime

import androidx.compose.runtime.ComposeNode
import assertk.assertFailure
import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.hasMessage
import assertk.assertions.isEmpty
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlinx.coroutines.test.runTest
import okio.Buffer

class MarkdownApplierTest {
  private lateinit var buffer: Buffer

  @BeforeTest fun setup() {
    buffer = Buffer()
  }

  @Test fun stackTexts() = runTest {
    buffer.markdown {
      repeat(3) { index ->
        Text("[#$index] Hello World!")
      }
    }
    assertThat(buffer.readUtf8().lines()).containsExactly(
      "[#0] Hello World!",
      "[#1] Hello World!",
      "[#2] Hello World!",
    )
  }

  @Test fun columnTexts() = runTest {
    buffer.markdown {
      Column {
        Text("Hello, World!")
        Text("Wubba Lubba Dub Dub!")
        Text("Bye, World!")
      }
    }
    assertThat(buffer.readUtf8().lines()).containsExactly(
      "Hello, World!",
      "<br/>",
      "Wubba Lubba Dub Dub!",
      "<br/>",
      "Bye, World!",
    )
  }

  @Test fun blackholeColumn() = runTest {
    buffer.markdown { Column {} }
    assertThat(buffer.readUtf8()).isEmpty()
  }

  @Test fun mixedTexts() = runTest {
    buffer.markdown {
      repeat(3) { index ->
        Text("[#$index] Hello World!")
      }
      Column {
        repeat(3) { index ->
          Text("[#$index] Bye World!")
        }
      }
      Column {}
    }
    assertThat(buffer.readUtf8().lines()).containsExactly(
      "[#0] Hello World!",
      "[#1] Hello World!",
      "[#2] Hello World!",
      "[#0] Bye World!",
      "<br/>",
      "[#1] Bye World!",
      "<br/>",
      "[#2] Bye World!",
      "",
    )
  }

  @Test fun nestedMarkdownIsNotAllowed() = runTest {
    val failure = assertFailure {
      buffer.markdown {
        ComposeNode<MarkdownNode, MarkdownApplier>(
          factory = { MarkdownLayoutNode(MarkdownOptions(), Buffer()) },
          update = {},
        )
      }
    }
    failure.hasMessage("Nested markdown is not supported.")
  }

  @Test fun nestedGroupIsNotAllowed() = runTest {
    val failure = assertFailure {
      buffer.markdown {
        Column { Column {} }
      }
    }
    failure.hasMessage("Nested group is not supported.")
  }
}
