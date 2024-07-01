/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/compose-markdown/blob/main/LICENSE
 */

package land.sungbin.markdown.runtime

import androidx.compose.runtime.Applier
import androidx.compose.runtime.collection.MutableVector

// AbstractApplier uses MutableList, but I want to use MutableVector.
public class MarkdownApplier internal constructor(
  private val root: MarkdownNode = MarkdownNode(kind = MarkdownKind.GROUP, contentTag = { "" }),
) : Applier<MarkdownNode> {
  private val stack = MutableVector<MarkdownNode>(capacity = 10).apply { add(root) }
  override var current: MarkdownNode = root

  init {
    require(root.group) { "Root node must be a group." }
  }

  override fun insertTopDown(index: Int, instance: MarkdownNode) {
    check(!current.text) { "Cannot add children to a text node." }

    if (current.footnote && instance.footnote) {
      throw MarkdownException("Footnotes cannot be nested.")
    }

    instance.index = index
    current.children.add(instance)
  }

  override fun down(node: MarkdownNode) {
    stack += node
    current = node
  }

  override fun up() {
    check(stack.size > 1) { "stack is root. cannot up()." }
    stack.removeAt(stack.lastIndex)
    current = stack.last()
  }

  override fun clear() {
    val current = current
    runtimeCheck(current === root) { "Unexpected clear() call on non-root node." }
  }

  override fun insertBottomUp(index: Int, instance: MarkdownNode) {
    // Ignored. Creating markdown is ideally top-down.
  }

  override fun remove(index: Int, count: Int) {
    notSupportedOperation("remove")
  }

  override fun move(from: Int, to: Int, count: Int) {
    notSupportedOperation("move")
  }

  private fun notSupportedOperation(operation: String): Nothing =
    throw NotImplementedError(
      "Dynamic layouts('$operation' operations) are not currently supported. " +
        "Try removing recomposition occurrences.",
    )
}
