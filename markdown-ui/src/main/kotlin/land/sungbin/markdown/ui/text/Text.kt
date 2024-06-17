package land.sungbin.markdown.ui.text

import androidx.annotation.DoNotInline
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.NonRestartableComposable
import land.sungbin.markdown.runtime.MarkdownApplier
import land.sungbin.markdown.runtime.MarkdownComposable
import land.sungbin.markdown.runtime.MarkdownNode
import land.sungbin.markdown.runtime.MarkdownOptions
import land.sungbin.markdown.ui.EmptyUpdater
import land.sungbin.markdown.ui.bufferOf
import land.sungbin.markdown.ui.modifier.Modifier
import land.sungbin.markdown.ui.modifier.applyTo
import okio.BufferedSink
import okio.ByteString
import okio.Source

@Immutable
private class MarkdownText(
  private val modifier: Modifier,
  private val sink: (MarkdownOptions) -> BufferedSink,
) : MarkdownNode.Text {
  private var snapshot: ByteString? = null

  override fun render(options: MarkdownOptions): Source {
    val source = modifier.applyTo(sink.invoke(options), options).buffer.clone()
    return source.also { snapshot = it.snapshot() }
  }

  override fun toString(): String = snapshot?.utf8() ?: "<not rendered yet>"
}

@DoNotInline
@[Composable NonRestartableComposable MarkdownComposable]
public fun Text(value: CharSequence, modifier: Modifier = Modifier) {
  val sink = { options: MarkdownOptions ->
    if (value is AbstractText) value.lazyWriting(options).buffer.clone()
    else bufferOf(value)
  }

  ComposeNode<MarkdownNode, MarkdownApplier>(
    factory = { MarkdownText(modifier, sink) },
    update = EmptyUpdater,
  )
}
