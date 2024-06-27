package land.sungbin.markdown.runtime

import okio.Buffer
import okio.ForwardingSource
import okio.Source
import okio.Timeout

public class MarkdownSource(
  source: Source,
  public val token: MarkdownToken,
) : ForwardingSource(source) {
  public companion object {
    private data object EmptySource : Source {
      override fun read(sink: Buffer, byteCount: Long): Long = -1L
      override fun timeout(): Timeout = Timeout.NONE
      override fun close() {}
    }

    public val END_QUOTE: MarkdownSource = MarkdownSource(EmptySource, MarkdownToken.EndQuote)
    public val END_CODE: MarkdownSource = MarkdownSource(EmptySource, MarkdownToken.EndCode)
    public val END_LIST: MarkdownSource = MarkdownSource(EmptySource, MarkdownToken.EndList)
    public val END_FOOTNOTE: MarkdownSource = MarkdownSource(EmptySource, MarkdownToken.EndFootnote)

    public val END_DOCUMENT: MarkdownSource = MarkdownSource(EmptySource, MarkdownToken.EndDocument)
  }
}
