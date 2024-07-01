/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/compose-markdown/blob/main/LICENSE
 */

package land.sungbin.markdown.ui.modifier

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import land.sungbin.markdown.runtime.MarkdownComposable
import land.sungbin.markdown.ui.text.TextTransformer

private val DefaultFootnotePosition: (String) -> Int = { it.length }

@Stable
public fun Modifier.footnote(
  tag: String,
  position: (String) -> Int = DefaultFootnotePosition,
  message: @Composable @MarkdownComposable () -> Unit,
): Modifier =
  this then FootnoteModifier(tag, position) with FootnoteGroup(tag, message)

// TODO make public
internal class FootnoteModifier(
  internal val tag: String,
  private val position: (String) -> Int = DefaultFootnotePosition,
) : TextTransformer {
  override fun transform(value: String): String = when (val position = position(value)) {
    0 -> "[^$tag]$value"
    value.length - 1 -> "$value[^$tag]"
    else -> value.substring(0, position + 1) + "[^$tag]" + value.substring(position + 1)
  }
}

internal data class FootnoteGroup(
  val tag: String,
  val content: @Composable () -> Unit,
)
