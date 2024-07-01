/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/compose-markdown/blob/main/LICENSE
 */

package land.sungbin.markdown.ui.image

import androidx.annotation.VisibleForTesting
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import land.sungbin.markdown.runtime.MarkdownComposable
import land.sungbin.markdown.ui.modifier.Modifier
import land.sungbin.markdown.ui.text.Text
import land.sungbin.markdown.ui.unit.Size

@VisibleForTesting
internal fun buildImageText(
  url: String,
  size: Size? = null,
  alt: String? = null,
): String = buildString {
  append("<img ")
  append("src=\"$url\" ")
  if (size != null) {
    val (width, height) = size
    append("width=\"$width\" height=\"$height\" ")
  }
  if (alt != null) append("alt=\"$alt\" ")
  append("/>")
}

@[Composable NonRestartableComposable MarkdownComposable]
public fun Image(
  url: String,
  size: Size? = null,
  alt: String? = null,
  modifier: Modifier = Modifier,
) {
  Text(modifier = modifier, value = buildImageText(url, size, alt))
}
