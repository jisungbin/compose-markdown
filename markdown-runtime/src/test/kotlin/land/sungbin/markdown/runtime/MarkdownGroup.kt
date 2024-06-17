package land.sungbin.markdown.runtime

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import kotlin.math.max
import okio.Buffer
import okio.Source

class MarkdownGroup : MarkdownNode.Group {
  private var texts = arrayOfNulls<MarkdownNode.Text>(10)
  private var maxIndex = -1

  override fun insert(index: Int, text: MarkdownNode.Text) {
    maxIndex = max(maxIndex, index)
    ensureCapacity(maxIndex + 1)
    texts[index] = text
  }

  override fun render(options: MarkdownOptions): Source {
    val buffer = Buffer()
    if (maxIndex < 0) return buffer

    for (index in 0..maxIndex) {
      val text = checkNotNull(texts[index]) { "Text at index $index is null. This should not happen." }
      buffer.writeAll(text.render(options))
      if (index < maxIndex) buffer.writeUtf8("\n").writeUtf8(options.newLineCharacter).writeUtf8("\n")
    }

    return buffer
  }

  private fun ensureCapacity(size: Int) {
    if (texts.size < size) {
      texts = texts.copyOf(size + 10)
    }
  }
}

@Composable
fun Column(texts: @Composable () -> Unit) {
  ComposeNode<MarkdownNode, MarkdownApplier>(
    factory = { MarkdownGroup() },
    update = {},
    content = texts,
  )
}
