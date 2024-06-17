package land.sungbin.markdown.runtime

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import okio.Buffer
import okio.Source

class MarkdownText(private val text: String) : MarkdownNode.Text {
  override fun render(options: MarkdownOptions): Source = Buffer().writeUtf8(text)
  override fun toString(): String = text
}

@Composable
fun Text(text: String) {
  ComposeNode<MarkdownNode, MarkdownApplier>(
    factory = { MarkdownText(text) },
    update = {},
  )
}
