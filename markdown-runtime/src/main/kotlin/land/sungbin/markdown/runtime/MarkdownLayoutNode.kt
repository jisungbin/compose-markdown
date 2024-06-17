/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/compose-markdown/blob/main/LICENSE
 */

package land.sungbin.markdown.runtime

import okio.Buffer

internal class MarkdownLayoutNode(
  override val options: MarkdownOptions,
  override val buffer: Buffer,
) : MarkdownNode.Root {
  override fun toString(): String = "MarkdownLayoutNode@${hashCode()}"
}
