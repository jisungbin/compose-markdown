/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/compose-markdown/blob/main/LICENSE
 */

package land.sungbin.markdown.runtime

public data class MarkdownOptions(
  public val defaultLanguage: String = "",
) {
  public companion object {
    public val Default: MarkdownOptions = MarkdownOptions()
  }
}
