/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/compose-markdown/blob/main/LICENSE
 */

package land.sungbin.markdown.runtime

internal class MarkdownLayoutNode(override val buffer: MutableList<MarkdownSource>) : MarkdownNode.Root {
  override fun toString(): String = "MarkdownLayoutNode@${hashCode()}"
}
