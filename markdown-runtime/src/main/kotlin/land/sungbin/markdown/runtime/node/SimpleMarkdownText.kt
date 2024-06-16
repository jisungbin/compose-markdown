/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/compose-markdown/blob/main/LICENSE
 */

package land.sungbin.markdown.runtime.node

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ComposeNode
import land.sungbin.markdown.runtime.MarkdownApplier
import land.sungbin.markdown.runtime.MarkdownComposable
import land.sungbin.markdown.runtime.MarkdownNode
import land.sungbin.markdown.runtime.MarkdownOptions
import okio.Buffer
import okio.Source

@PublishedApi
@JvmInline
internal value class SimpleMarkdownText(private val text: String) : MarkdownNode.Text {
  override fun render(options: MarkdownOptions): Source = Buffer().writeUtf8(text)
  override fun toString(): String = text
}

@Suppress("NOTHING_TO_INLINE")
@MarkdownComposable
@Composable
public inline fun Text(text: String) {
  ComposeNode<MarkdownNode, MarkdownApplier>(
    factory = { SimpleMarkdownText(text) },
    update = EmptyUpdater,
  )
}
