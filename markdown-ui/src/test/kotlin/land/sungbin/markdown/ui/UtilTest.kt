package land.sungbin.markdown.ui

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isNotEqualTo
import assertk.assertions.isTrue
import kotlin.test.Test

class UtilTest {
  @Test fun bufferOfString() {
    val target = "Hello, World! 안녕, 세계!"
    val buffer = bufferOf(target)
    assertThat(buffer.readUtf8()).isEqualTo(target)
  }

  @Test fun bufferOfAbstractTextDoesntConsuming() {
    val target = TextForTest("Hello, World! 안녕, 세계!")
    val buffer = bufferOf(target)

    assertThat(target).isEqualTo(buffer)

    target.source { readUtf8() }

    assertThat(target.buffer.exhausted()).isTrue()
    assertThat(buffer.buffer.exhausted()).isFalse()

    val targetSnapshot = target.buffer.snapshot()
    val bufferSnapshot = buffer.snapshot()

    buffer.writeUtf8("Bye, World! 잘가, 세계!")

    assertThat(target.buffer.snapshot()).isEqualTo(targetSnapshot)
    assertThat(buffer.snapshot()).isNotEqualTo(bufferSnapshot)
  }

  @Test fun fastRepeat() {
    val numbers = mutableListOf<Int>()
    fastRepeat(10, block = numbers::add)
    assertThat(numbers).containsExactly(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
  }
}