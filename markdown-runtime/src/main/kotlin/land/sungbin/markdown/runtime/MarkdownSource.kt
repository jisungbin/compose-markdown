package land.sungbin.markdown.runtime

import okio.BufferedSource
import okio.ForwardingSource
import okio.Source
import okio.buffer

public class MarkdownSource(
  source: Source,
  public val token: MarkdownToken,
) : ForwardingSource(source) {
  public val buffer: BufferedSource by lazy { buffer() }

  internal companion object {
    internal val END_DOCUMENT = MarkdownSource(EmptySource, MarkdownToken.EndDocument)
  }
}
