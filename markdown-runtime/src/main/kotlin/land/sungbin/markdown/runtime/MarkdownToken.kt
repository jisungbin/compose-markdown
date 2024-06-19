package land.sungbin.markdown.runtime

public sealed class MarkdownToken {
  public data object Text : MarkdownToken()
  public data object NewLine : MarkdownToken()

  public data object BeginQuote : MarkdownToken()
  public data object EndQuote : MarkdownToken()

  public class BeginCode(public val language: String) : MarkdownToken()
  public data object EndCode : MarkdownToken()

  public class BeginFootnote(public val tag: String) : MarkdownToken()
  public data object EndFootnote : MarkdownToken()

  public data object BeginList : MarkdownToken()
  public data object EndList : MarkdownToken()

  /**
   * The end of the Markdown stream. This sentinel value is returned by
   * [MarkdownReader.peek] to signal that the Markdown-encoded value has no more tokens.
   */
  internal data object EndDocument : MarkdownToken()
}
