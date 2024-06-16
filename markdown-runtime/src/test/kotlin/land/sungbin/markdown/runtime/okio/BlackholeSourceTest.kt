/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/compose-markdown/blob/main/LICENSE
 */

package land.sungbin.markdown.runtime.okio

import assertk.assertThat
import assertk.assertions.isSameInstanceAs
import assertk.assertions.isZero
import kotlin.test.Test
import okio.Timeout
import okio.buffer

class BlackholeSourceTest {
  @Test fun sizeReturnsZero() {
    val source = blackholeSource()
    val buffer = source.buffer().apply { request(Long.MAX_VALUE) }.buffer
    assertThat(buffer.size).isZero()
  }

  @Test fun noTimeout() {
    val source = blackholeSource()
    assertThat(source.timeout()).isSameInstanceAs(Timeout.NONE)
  }
}
