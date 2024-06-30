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
  private val root: MarkdownNode = MarkdownNode(kind = MarkdownKind.GROUP),
  private val footnotes: MarkdownNode = MarkdownNode(kind = MarkdownKind.GROUP),
) : Applier<MarkdownNode> {
  private val stack = MutableVector<MarkdownNode>(capacity = 10).apply { add(root) }
  override var current: MarkdownNode = root

  init {
    require(root.group) { "Root node must be a group." }
    require(footnotes.group) { "Footnotes node must be a group." }
  }

  override fun insertTopDown(index: Int, instance: MarkdownNode) {
    runtimeCheck(!current.text) { "Cannot add children to a text node." }

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
    runtimeCheck(stack.isNotEmpty()) { "stack is empty. cannot up()." }

    val tail = current

    stack.removeAt(stack.lastIndex)
    current = stack.last()

    if (tail.text) return // text can't be a group, so at this point it's already child of current.
    when {
      current.text -> runtimeError { "Text nodes cannot have children." }
      current.group -> current.children.add(tail)
      current.footnote -> footnotes.children.add(tail)
    }
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
