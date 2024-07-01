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
import land.sungbin.markdown.ui.modifier.Modifier

@[Composable NonRestartableComposable MarkdownComposable]
public fun Header(
  level: Int,
  text: CharSequence,
  modifier: Modifier = Modifier,
) {
  Text(modifier = modifier, value = "${"#".repeat(level)} $text")
}

@Suppress("NOTHING_TO_INLINE")
@[Composable NonRestartableComposable MarkdownComposable]
public inline fun H1(text: CharSequence, modifier: Modifier = Modifier) {
  Header(level = 1, text = text, modifier = modifier)
}

@Suppress("NOTHING_TO_INLINE")
@[Composable NonRestartableComposable MarkdownComposable]
public inline fun H2(text: CharSequence, modifier: Modifier = Modifier) {
  Header(level = 2, text = text, modifier = modifier)
}

@Suppress("NOTHING_TO_INLINE")
@[Composable NonRestartableComposable MarkdownComposable]
public inline fun H3(text: CharSequence, modifier: Modifier = Modifier) {
  Header(level = 3, text = text, modifier = modifier)
}

@Suppress("NOTHING_TO_INLINE")
@[Composable NonRestartableComposable MarkdownComposable]
public inline fun H4(text: CharSequence, modifier: Modifier = Modifier) {
  Header(level = 4, text = text, modifier = modifier)
}

@Suppress("NOTHING_TO_INLINE")
@[Composable NonRestartableComposable MarkdownComposable]
public inline fun H5(text: CharSequence, modifier: Modifier = Modifier) {
  Header(level = 5, text = text, modifier = modifier)
}

@Suppress("NOTHING_TO_INLINE")
@[Composable NonRestartableComposable MarkdownComposable]
public inline fun H6(text: CharSequence, modifier: Modifier = Modifier) {
  Header(level = 6, text = text, modifier = modifier)
}
