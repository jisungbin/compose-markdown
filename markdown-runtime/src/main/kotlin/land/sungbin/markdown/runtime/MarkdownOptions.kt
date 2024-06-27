package land.sungbin.markdown.runtime

public data class MarkdownOptions(
  public val defaultLanguage: String = "",
) {
  public companion object {
    public val Default: MarkdownOptions = MarkdownOptions()
  }
}
