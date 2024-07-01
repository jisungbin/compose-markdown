/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/compose-markdown/blob/main/LICENSE
 */

package land.sungbin.markdown.ui.modifier

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import land.sungbin.markdown.runtime.MarkdownOptions
import land.sungbin.markdown.ui.text.TextTransformer

private val DefaultClickableRange: (String) -> IntRange = { 0..it.length }

@Stable
public fun Modifier.clickable(link: String, range: (String) -> IntRange = DefaultClickableRange): Modifier =
  this then ClickableTransformer(link, range)

@Immutable
public class ClickableTransformer(
  private val link: String,
  private val range: (String) -> IntRange,
) : TextTransformer {
  override fun transform(options: MarkdownOptions, value: String): String {
    val originalLength = value.length
    val linkLength = link.length
    val replacementRange = range(value)

    return CharArray(originalLength + 4 + linkLength).also { new ->
      if (replacementRange.first > 0) {
        value.toCharArray(new, 0, 0, replacementRange.first)
      }
      new[replacementRange.first] = '['
      value.toCharArray(new, replacementRange.first + 1, replacementRange.first, replacementRange.last + 1)
      new[replacementRange.last + 2] = ']'
      new[replacementRange.last + 3] = '('
      link.toCharArray(new, replacementRange.last + 4, 0, linkLength)
      new[replacementRange.last + 4 + linkLength] = ')'
      if (replacementRange.last + 1 < originalLength) {
        value.toCharArray(new, replacementRange.last + 4 + linkLength + 1, replacementRange.last + 1, originalLength)
      }
    }
      .concatToString()
  }
}
