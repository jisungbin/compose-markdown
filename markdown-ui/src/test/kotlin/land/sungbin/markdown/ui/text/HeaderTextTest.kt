package land.sungbin.markdown.ui.text

import assertk.assertFailure
import assertk.assertThat
import assertk.assertions.hasMessage
import assertk.assertions.isEqualTo
import kotlin.test.Test
import land.sungbin.markdown.ui.bufferOf

class HeaderTextTest {
  @Test fun validRanges() {
    var failure = assertFailure { HeaderText(0, bufferOf("")) }
    failure.hasMessage("Header level should be in 1..6")

    failure = assertFailure { HeaderText(7, bufferOf("")) }
    failure.hasMessage("Header level should be in 1..6")
  }

  @Test fun h1() {
    val header = HeaderText(1, bufferOf("Header 1 첫 번째"))
    assertThat(header.source { readUtf8() }).isEqualTo("# Header 1 첫 번째")
  }

  @Test fun h2() {
    val header = HeaderText(2, bufferOf("Header 2 두 번째"))
    assertThat(header.source { readUtf8() }).isEqualTo("## Header 2 두 번째")
  }

  @Test fun h3() {
    val header = HeaderText(3, bufferOf("Header 3 세 번째"))
    assertThat(header.source { readUtf8() }).isEqualTo("### Header 3 세 번째")
  }

  @Test fun h4() {
    val header = HeaderText(4, bufferOf("Header 4 네 번째"))
    assertThat(header.source { readUtf8() }).isEqualTo("#### Header 4 네 번째")
  }

  @Test fun h5() {
    val header = HeaderText(5, bufferOf("Header 5 다섯 번째"))
    assertThat(header.source { readUtf8() }).isEqualTo("##### Header 5 다섯 번째")
  }

  @Test fun h6() {
    val header = HeaderText(6, bufferOf("Header 6 여섯 번째"))
    assertThat(header.source { readUtf8() }).isEqualTo("###### Header 6 여섯 번째")
  }
}