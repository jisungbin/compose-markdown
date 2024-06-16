/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/compose-markdown/blob/main/LICENSE
 */

package land.sungbin.markdown.runtime.okio

import okio.Buffer
import okio.Source
import okio.Timeout

/** Returns a [Source] that reads nowhere. */
internal fun blackholeSource(): Source = BlackholeSource

private object BlackholeSource : Source {
  override fun read(sink: Buffer, byteCount: Long): Long = -1L
  override fun timeout(): Timeout = Timeout.NONE
  override fun close() {}
}
