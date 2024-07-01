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
import androidx.compose.ui.util.fastForEach
import land.sungbin.markdown.runtime.MarkdownApplier
import land.sungbin.markdown.runtime.MarkdownComposable
import land.sungbin.markdown.runtime.MarkdownKind
import land.sungbin.markdown.runtime.MarkdownNode
import land.sungbin.markdown.ui.EmptyUpdater
import land.sungbin.markdown.ui.modifier.Modifier
import land.sungbin.markdown.ui.modifier.MutableModifier
import land.sungbin.markdown.ui.modifier.applyTo

@[Composable NonRestartableComposable MarkdownComposable]
public fun Text(value: CharSequence, modifier: Modifier = Modifier) {
  ComposeNode<MarkdownNode, MarkdownApplier>(
    factory = { MarkdownNode(source = { options -> modifier.applyTo(options, value.toString()) }) },
    update = EmptyUpdater,
    content = {
      if (modifier is MutableModifier) {
        modifier.footnotes.fastForEach { (tag, content) ->
          ComposeNode<MarkdownNode, MarkdownApplier>(
            factory = { MarkdownNode(kind = MarkdownKind.FOOTNOTE, contentTag = { _, _ -> "[^$tag]: " }) },
            update = EmptyUpdater,
            content = content,
          )
        }
      }
    },
  )
}
