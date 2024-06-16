/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/compose-markdown/blob/main/LICENSE
 */

package land.sungbin.markdown.runtime

import androidx.compose.runtime.Immutable
import okio.Path

@Immutable
public data class MarkdownOptions(
  public val listIndent: Int = 3,
  public val newLineCharacter: String = "<br/>",
  public val codeBlockDefaultLanguage: String? = null,
  public val defaultImageStorage: Path? = null,
) {
  public companion object {
    public val Default: MarkdownOptions = MarkdownOptions()
  }
}
