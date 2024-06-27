package land.sungbin.markdown.runtime

public sealed class MarkdownToken {
  public sealed interface Begin
  public sealed interface End

  public data object Text : MarkdownToken()
  public data object NewLine : MarkdownToken()

  public data object BeginQuote : MarkdownToken(), Begin
  public data object EndQuote : MarkdownToken(), End

  public data class BeginCode(public val language: String? = null) : MarkdownToken(), Begin
  public data object EndCode : MarkdownToken(), End

  public data class BeginList(public val type: Type) : MarkdownToken(), Begin {
    public enum class Type {
      Ordered,
      Unordered,
    }
  }

  public data object EndList : MarkdownToken(), End

  public data class BeginFootnote(public val tag: String) : MarkdownToken(), Begin
  public data object EndFootnote : MarkdownToken(), End

  /**
   * The end of the markdown stream. At this point, you can handle nodes that should be
   * rendered at the bottom of the document, such as footnotes.
   */
  internal data object EndDocument : MarkdownToken()
}
