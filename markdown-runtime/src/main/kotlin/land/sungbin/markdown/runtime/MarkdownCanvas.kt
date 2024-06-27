/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/compose-markdown/blob/main/LICENSE
 */

package land.sungbin.markdown.runtime

import androidx.compose.runtime.collection.MutableVector

internal class MarkdownCanvas(
  override val contents: MutableVector<MarkdownSource>,
  override val footnotes: MutableVector<MarkdownSource>,
) : MarkdownNode.Root
