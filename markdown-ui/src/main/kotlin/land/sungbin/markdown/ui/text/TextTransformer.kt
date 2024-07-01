/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/compose-markdown/blob/main/LICENSE
 */

package land.sungbin.markdown.ui.text

import androidx.compose.runtime.Immutable
import land.sungbin.markdown.runtime.MarkdownOptions

@Immutable
public fun interface TextTransformer {
  public fun transform(options: MarkdownOptions, value: String): String
}
