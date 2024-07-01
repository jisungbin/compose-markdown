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

class HeadersTest {
  @Test fun h1(): Unit = runTest {
    val result = markdown {
      H1("Hello, World!")
    }
    assertThat(result).isEqualTo("# Hello, World!")
  }

  @Test fun h2(): Unit = runTest {
    val result = markdown {
      H2("Hello, World!")
    }
    assertThat(result).isEqualTo("## Hello, World!")
  }

  @Test fun h3(): Unit = runTest {
    val result = markdown {
      H3("Hello, World!")
    }
    assertThat(result).isEqualTo("### Hello, World!")
  }

  @Test fun h4(): Unit = runTest {
    val result = markdown {
      H4("Hello, World!")
    }
    assertThat(result).isEqualTo("#### Hello, World!")
  }

  @Test fun h5(): Unit = runTest {
    val result = markdown {
      H5("Hello, World!")
    }
    assertThat(result).isEqualTo("##### Hello, World!")
  }

  @Test fun h6(): Unit = runTest {
    val result = markdown {
      H6("Hello, World!")
    }
    assertThat(result).isEqualTo("###### Hello, World!")
  }
}
