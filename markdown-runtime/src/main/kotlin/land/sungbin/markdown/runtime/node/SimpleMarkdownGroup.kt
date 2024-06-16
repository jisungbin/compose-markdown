/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/compose-markdown/blob/main/LICENSE
 */

package land.sungbin.markdown.runtime.node

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import kotlin.math.max
import land.sungbin.markdown.runtime.MarkdownApplier
import land.sungbin.markdown.runtime.MarkdownComposable
import land.sungbin.markdown.runtime.MarkdownNode
import land.sungbin.markdown.runtime.MarkdownOptions
import land.sungbin.markdown.runtime.okio.blackholeSource
import okio.Buffer
import okio.Source

@PublishedApi
internal class SimpleMarkdownGroup private constructor() : MarkdownNode.Group {
  private var texts = arrayOfNulls<MarkdownNode.Text>(10)
  private var maxIndex = -1

  override fun insert(index: Int, text: MarkdownNode.Text) {
    maxIndex = max(maxIndex, index)
    ensureCapacity(maxIndex + 1)
    texts[index] = text
  }

  override fun render(options: MarkdownOptions): Source {
    if (maxIndex < 0) return blackholeSource()

    val buffer = Buffer()
    for (index in 0..maxIndex) {
      val text = checkNotNull(texts[index]) { "Text at index $index is null. This should not happen." }
      buffer.writeAll(text.render(options))
      if (index < maxIndex) buffer.writeUtf8("\n").writeUtf8(options.newLineCharacter).writeUtf8("\n")
    }

    return buffer
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as SimpleMarkdownGroup

    return texts contentEquals other.texts
  }

  override fun hashCode(): Int = texts.hashCode()

  override fun toString(): String = "SimpleMarkdownGroup@${hashCode()}"

  private fun ensureCapacity(size: Int) {
    if (texts.size < size) {
      texts = texts.copyOf(size + 10)
    }
  }

  @PublishedApi
  internal companion object {
    @PublishedApi
    internal val Constructor: () -> MarkdownNode = ::SimpleMarkdownGroup
  }
}

@MarkdownComposable
@Composable
public inline fun Column(texts: @MarkdownComposable @Composable () -> Unit) {
  ComposeNode<MarkdownNode, MarkdownApplier>(
    factory = SimpleMarkdownGroup.Constructor,
    update = EmptyUpdater,
    content = texts,
  )
}
