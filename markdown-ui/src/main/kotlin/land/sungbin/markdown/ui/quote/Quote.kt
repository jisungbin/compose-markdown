package land.sungbin.markdown.ui.quote

import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import land.sungbin.markdown.runtime.MarkdownComposable
import land.sungbin.markdown.ui.bufferOf
import land.sungbin.markdown.ui.modifier.Modifier
import land.sungbin.markdown.ui.text.AbstractText
import land.sungbin.markdown.ui.text.Text
import okio.BufferedSource

@PublishedApi
internal class QuoteText(value: BufferedSource) : AbstractText() {
  init {
    sink {
      writeByte('>'.code)
      writeByte(' '.code)
      writeAll(value)
    }
  }
}

@Suppress("NOTHING_TO_INLINE")
@[Composable NonRestartableComposable MarkdownComposable]
public inline fun Quote(
  text: CharSequence,
  modifier: Modifier = Modifier,
) {
  Text(modifier = modifier, value = QuoteText(bufferOf(text)))
}
