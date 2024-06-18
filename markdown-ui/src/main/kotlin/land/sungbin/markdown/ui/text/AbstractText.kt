package land.sungbin.markdown.ui.text

import androidx.compose.runtime.Stable
import okio.Buffer
import okio.BufferedSink
import okio.BufferedSource
import okio.ByteString

@Stable
public abstract class AbstractText internal constructor() : CharSequence {
  @PublishedApi
  internal val buffer: Buffer = Buffer()

  final override val length: Int get() = buffer.size.toInt()

  final override fun get(index: Int): Char = buffer[index.toLong()].toInt().toChar()

  internal inline fun <T> source(action: BufferedSource.() -> T): T = action(buffer)
  internal inline fun <T> sink(action: BufferedSink.() -> T): T = action(buffer)

  final override fun subSequence(startIndex: Int, endIndex: Int): CharSequence =
    buffer.snapshot().substring(startIndex, endIndex).asCharSequence()

  override fun toString(): String = buffer.snapshot().utf8()

  override fun hashCode(): Int = buffer.hashCode()
  override fun equals(other: Any?): Boolean = buffer == other

  private fun ByteString.asCharSequence(): CharSequence = object : CharSequence {
    override val length: Int get() = this@asCharSequence.size

    override fun get(index: Int): Char = this@asCharSequence[index].toInt().toChar()

    override fun subSequence(startIndex: Int, endIndex: Int): CharSequence =
      this@asCharSequence.substring(startIndex, endIndex).asCharSequence()

    override fun toString(): String = this@asCharSequence.utf8()
  }
}
