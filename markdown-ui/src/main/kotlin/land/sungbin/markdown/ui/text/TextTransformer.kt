package land.sungbin.markdown.ui.text

import androidx.compose.runtime.Immutable
import land.sungbin.markdown.runtime.MarkdownOptions
import okio.BufferedSink

@Immutable
public fun interface TextTransformer {
  public fun transform(sink: BufferedSink, options: MarkdownOptions): BufferedSink
}
