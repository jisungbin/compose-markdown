@file:Suppress("unused")

package land.sungbin.markdown.ui.text

import androidx.compose.runtime.Stable
import land.sungbin.markdown.runtime.MarkdownOptions
import okio.Buffer
import okio.BufferedSink
import okio.BufferedSource

@Stable
public abstract class AbstractText internal constructor() : CharSequence {
  @PublishedApi
  internal val buffer: Buffer = Buffer()

  final override val length: Int get() = buffer.size.toInt()

  final override fun get(index: Int): Char = buffer[index.toLong()].toInt().toChar()

  internal inline fun <T> read(action: BufferedSource.() -> T): T = buffer.clone().use(action)
  internal inline fun <T> write(action: BufferedSink.() -> T): T = buffer.run(action)

  @PublishedApi
  internal open fun lazyWriting(options: MarkdownOptions): AbstractText = this

  final override fun subSequence(startIndex: Int, endIndex: Int): CharSequence =
    buffer.snapshot().substring(startIndex, endIndex).utf8()

  override fun toString(): String = buffer.snapshot().utf8()

  override fun hashCode(): Int = buffer.hashCode()
  override fun equals(other: Any?): Boolean = buffer == other
}
