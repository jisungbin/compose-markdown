package land.sungbin.markdown.ui.text

import androidx.compose.runtime.Stable
import okio.BufferedSink

@Stable
public fun interface TextTransformer {
  public fun transform(sink: BufferedSink): BufferedSink
}
