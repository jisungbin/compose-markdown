/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/compose-markdown/blob/main/LICENSE
 */

package land.sungbin.markdown.runtime

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import kotlin.math.max
import okio.Buffer
import okio.Source

class MarkdownGroup : MarkdownNode.Group {
  private var texts = arrayOfNulls<MarkdownNode.Text>(4096)
  private var maxIndex = -1

  override fun insert(index: Int, text: MarkdownNode.Text) {
    maxIndex = max(maxIndex, index)
    texts[index] = text
  }

  override fun render(): Source {
    val buffer = Buffer()
    if (maxIndex < 0) return buffer

    for (index in 0..maxIndex) {
      val text = checkNotNull(texts[index]) { "Text at index $index is null. This should not happen." }
      buffer.writeAll(text.render())
      if (index < maxIndex) buffer.writeUtf8("\n").writeUtf8("<br/>").writeUtf8("\n")
    }

    return buffer
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
