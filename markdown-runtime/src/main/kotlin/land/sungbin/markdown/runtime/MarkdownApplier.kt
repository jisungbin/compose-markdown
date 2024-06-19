/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/compose-markdown/blob/main/LICENSE
 */

package land.sungbin.markdown.runtime

import androidx.compose.runtime.Applier
import androidx.compose.runtime.collection.MutableVector
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

// AbstractApplier uses MutableList, but I want to use MutableVector.
internal class MarkdownApplier(private val root: MarkdownNode.Root) : Applier<MarkdownNode> {
  // TODO consider whether a capacity of 50 is the best default value
  private val stack = MutableVector<MarkdownNode>(capacity = 50).apply { add(root) }
  override var current: MarkdownNode = root

  constructor(buffer: MutableList<MarkdownSource>) : this(MarkdownLayoutNode(buffer))

  override fun insertTopDown(index: Int, instance: MarkdownNode) {
    assertNotNestedRoot(instance)

    when (val current = current) {
      is MarkdownNode.Group -> {
        if (instance is MarkdownNode.Footnote && current is MarkdownNode.Footnote) {
          throw MarkdownException("Footnote cannot be nested.")
        }
        if (instance is MarkdownNode.Code && current is MarkdownNode.Code) {
          throw MarkdownException("Code cannot be nested.")
        }
        current.insert(index, instance)
      }
      is MarkdownNode.Root -> Unit // Root is handled by up().
      is MarkdownNode.Text -> error("Text node cannot have children.")
    }
  }

  override fun down(node: MarkdownNode) {
    stack += node
    current = node
  }

  override fun up() {
    check(stack.isNotEmpty()) { "stack is empty. cannot up()" }

    val tail = current
    assertNotNestedRoot(tail)

    stack.removeAt(stack.lastIndex)
    current = stack.last()

    val current = current
    if (current !is MarkdownNode.Root) return

    when (tail) {
      is MarkdownNode.Text -> current.buffer.add(tail.render())
      is MarkdownNode.Group -> {
        repeat(tail.size) {
          current.buffer.add(tail.render())
        }
      }
    }
  }

  override fun move(from: Int, to: Int, count: Int) {
    notSupportedOperation("move")
  }

  override fun remove(index: Int, count: Int) {
    notSupportedOperation("remove")
  }

  override fun clear() {
    val current = current
    if (current !is MarkdownNode.Root) {
      error("Unexpected clear() call on non-root node.")
    }
    current.buffer.add(MarkdownSource.END_DOCUMENT)
  }

  override fun insertBottomUp(index: Int, instance: MarkdownNode) {
    // Ignored. We use TopDown.
  }

  @OptIn(ExperimentalContracts::class)
  private fun assertNotNestedRoot(node: MarkdownNode) {
    contract { returns() implies (node is MarkdownNode.Renderable) }
    if (node is MarkdownNode.Root) {
      throw NotImplementedError("Nested markdown root is not supported.")
    }
  }

  private fun notSupportedOperation(operation: String): Nothing =
    throw NotImplementedError(
      "Operation $operation is not supported. Dynamic layouts are not currently " +
        "supported. (Did you try recomposition?)",
    )
}
