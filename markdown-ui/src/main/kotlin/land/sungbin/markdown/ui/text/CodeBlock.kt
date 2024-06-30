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
import land.sungbin.markdown.runtime.MarkdownKind
import land.sungbin.markdown.runtime.MarkdownNode
import land.sungbin.markdown.ui.EmptyUpdater

@[Composable NonRestartableComposable MarkdownComposable]
public inline fun Code(
  language: String? = null,
  content: @Composable @MarkdownComposable () -> Unit,
) {
  ComposeNode<MarkdownNode, MarkdownApplier>(
    factory = {
      MarkdownNode(
        kind = MarkdownKind.GROUP,
        contentKind = MarkdownKind.TEXT,
        contentTag = { _, options -> "```${language ?: options.defaultLanguage}" + '\n' },
      )
    },
    update = EmptyUpdater,
    content = {
      content()
      Text('\n' + "```")
    },
  )
}
