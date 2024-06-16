/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/compose-markdown/blob/main/LICENSE
 */

package land.sungbin.markdown.runtime.node

import land.sungbin.markdown.runtime.MarkdownNode
import land.sungbin.markdown.runtime.MarkdownOptions
import okio.Buffer

public class SimpleMarkdownRoot(
  override val options: MarkdownOptions,
  override val buffer: Buffer,
) : MarkdownNode.Root {
  override fun toString(): String = "SimpleMarkdownRoot@${hashCode()}"
}
