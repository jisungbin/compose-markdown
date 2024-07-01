/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/compose-markdown/blob/main/LICENSE
 */

package land.sungbin.markdown.ui.quote

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.test.Test
import kotlinx.coroutines.test.runTest
import land.sungbin.markdown.runtime.markdown
import land.sungbin.markdown.ui.text.Text

class QuoteTest {
  @Test fun singleQuote(): Unit = runTest {
    val result = markdown {
      Quote {
        Text("Hello, world!")
      }
    }
    assertThat(result).isEqualTo("> Hello, world!")
  }

  @Test fun multiQuotes(): Unit = runTest {
    val result = markdown {
      Quote {
        repeat(3) {
          Text("Hello, world!")
        }
      }
    }
    assertThat(result).isEqualTo(
      """
      > Hello, world!
      > Hello, world!
      > Hello, world!
      """.trimIndent(),
    )
  }

  @Test fun nestedQuotes(): Unit = runTest {
    val result = markdown {
      Quote {
        Text("Hello, world!")
        Quote {
          Text("Hello, world!")
          Quote {
            repeat(3) {
              Text("Hello, world!\nBye, world!")
            }
          }
        }
      }
    }
    assertThat(result).isEqualTo(
      """
      > Hello, world!
      > > Hello, world!
      > > > Hello, world!
      > > > Bye, world!
      > > > Hello, world!
      > > > Bye, world!
      > > > Hello, world!
      > > > Bye, world!
      """.trimIndent(),
    )
  }
}
