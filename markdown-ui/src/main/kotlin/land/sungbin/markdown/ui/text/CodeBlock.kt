/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/compose-markdown/blob/main/LICENSE
 */

package land.sungbin.markdown.ui.text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import land.sungbin.markdown.runtime.MarkdownComposable

@[Composable NonRestartableComposable MarkdownComposable]
public inline fun Code(
  language: String = "",
  content: @Composable @MarkdownComposable () -> Unit,
) {
  Text("```$language")
  content()
  Text("```")
}
