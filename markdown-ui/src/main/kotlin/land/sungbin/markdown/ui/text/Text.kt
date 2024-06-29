/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/compose-markdown/blob/main/LICENSE
 */

package land.sungbin.markdown.ui.text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import androidx.compose.runtime.NonRestartableComposable
import land.sungbin.markdown.runtime.MarkdownApplier
import land.sungbin.markdown.runtime.MarkdownComposable
import land.sungbin.markdown.runtime.MarkdownNode
import land.sungbin.markdown.runtime.MarkdownOptions
import land.sungbin.markdown.ui.EmptyUpdater
import land.sungbin.markdown.ui.modifier.Modifier
import land.sungbin.markdown.ui.modifier.applyTo
import okio.Buffer
import okio.BufferedSource

@Suppress("NOTHING_TO_INLINE")
@PublishedApi
internal inline fun sourceOf(modifier: Modifier, value: String): (MarkdownOptions) -> BufferedSource =
  { options ->
    val source = modifier.applyTo(options, Buffer().writeUtf8(value))
    source.buffer
  }

@Suppress("NOTHING_TO_INLINE")
@[Composable NonRestartableComposable MarkdownComposable]
public inline fun Text(value: CharSequence, modifier: Modifier = Modifier) {
  ComposeNode<MarkdownNode, MarkdownApplier>(
    factory = { MarkdownNode(source = sourceOf(modifier, value.toString())) },
    update = EmptyUpdater,
  )
}
