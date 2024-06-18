package land.sungbin.markdown.ui.text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import androidx.compose.runtime.NonRestartableComposable
import land.sungbin.markdown.runtime.MarkdownApplier
import land.sungbin.markdown.runtime.MarkdownComposable
import land.sungbin.markdown.runtime.MarkdownNode
import land.sungbin.markdown.ui.EmptyUpdater
import land.sungbin.markdown.ui.bufferOf
import land.sungbin.markdown.ui.modifier.Modifier
import land.sungbin.markdown.ui.modifier.applyTo
import okio.BufferedSink
import okio.ByteString
import okio.Source

@PublishedApi
internal class MarkdownText(
  private val modifier: Modifier,
  private val sink: BufferedSink,
) : MarkdownNode.Text {
  private var renderSnapshot: ByteString? = null

  override fun render(): Source {
    val source = modifier.applyTo(sink).buffer.clone()
    return source.also { renderSnapshot = it.snapshot() }
  }

  override fun toString(): String =
    renderSnapshot?.utf8() ?: "<not rendered yet; ${'$'}${sink.buffer.snapshot().utf8()}>"
}

@Suppress("NOTHING_TO_INLINE")
@[Composable NonRestartableComposable MarkdownComposable]
public inline fun Text(value: CharSequence, modifier: Modifier = Modifier) {
  ComposeNode<MarkdownNode, MarkdownApplier>(
    factory = { MarkdownText(modifier, bufferOf(value)) },
    update = EmptyUpdater,
  )
}
