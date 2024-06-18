package land.sungbin.markdown.ui.text

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import kotlin.test.BeforeTest
import kotlin.test.Test
import land.sungbin.markdown.ui.TextForTest
import okio.ByteString

class AbstractTextTest {
  private lateinit var text: AbstractText
  private var snapshot: ByteString? = null

  @BeforeTest fun prepare() {
    text = TextForTest()
    snapshot = null
  }

  @Test fun readAndWrite() {
    text.sink { writeUtf8("안녕하세요 hello") }
    snapshot = text.source { buffer.snapshot() }

    assertThat(snapshot?.utf8()).isEqualTo("안녕하세요 hello")

    text.sink { writeUtf8(" 잘가세요 bye") }
    snapshot = text.source { buffer.snapshot() }

    assertThat(snapshot?.utf8()).isEqualTo("안녕하세요 hello 잘가세요 bye")
  }

  // Korean is 3 bytes for each character
  @Test fun validSize() {
    text.sink { writeUtf8("안녕하세요 hello") }
    assertThat(text.length).isEqualTo(21)

    text.sink { writeUtf8(" 잘가세요 bye") }
    assertThat(text.length).isEqualTo(38)
  }

  @Test fun subSequenceDoesntConsumingOriginal() {
    text.sink { writeUtf8("안녕하세요 hello") }

    val subText = text.subSequence(12, 17)
    snapshot = text.source { buffer.snapshot() }

    assertThat(subText.length).isEqualTo(5)
    assertThat(subText.toString()).isEqualTo("요 h")
    assertThat(snapshot?.utf8()).isEqualTo("안녕하세요 hello")

    assertThat(text.buffer.exhausted()).isFalse()

    text.sink { writeUtf8(" 잘가세요 bye") }
    snapshot = text.source { buffer.snapshot() }

    assertThat(snapshot?.utf8()).isEqualTo("안녕하세요 hello 잘가세요 bye")
  }
}