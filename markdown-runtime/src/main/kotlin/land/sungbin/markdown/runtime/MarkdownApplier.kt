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
import land.sungbin.markdown.runtime.node.SimpleMarkdownRoot
import okio.Buffer

@PublishedApi
internal class MarkdownApplier(private val root: MarkdownNode.Root) : Applier<MarkdownNode> {
  private val stack = MutableVector<MarkdownNode>(capacity = 10).apply { add(root) }
  override var current: MarkdownNode = root

  constructor(options: MarkdownOptions, buffer: Buffer) :
    this(root = SimpleMarkdownRoot(options = options, buffer = buffer))

  override fun insertTopDown(index: Int, instance: MarkdownNode) {
    assertNotNestedRoot(instance)

    when (val current = current) {
      is MarkdownNode.Group -> {
        assertNotNestedGroup(instance)
        current.insert(index, instance)
      }
      is MarkdownNode.Root -> Unit // This is handled by up().
      is MarkdownNode.Text -> error("Text node cannot have children.")
    }
  }

  override fun down(node: MarkdownNode) {
    stack += node
    current = node
  }

  override fun up() {
    val tail = current

    check(stack.isNotEmpty()) { "empty stack" }
    stack.removeAt(stack.lastIndex)
    current = stack.last()

    val current = current
    if (current is MarkdownNode.Root && tail is MarkdownNode.Renderable) {
      if (current.buffer.exhausted()) {
        current.buffer.writeAll(tail.render(options = root.options))
      } else {
        current.buffer.writeUtf8("\n\n").writeAll(tail.render(options = root.options))
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
    // Nothing to do.
  }

  override fun insertBottomUp(index: Int, instance: MarkdownNode) {
    // Ignored. We use TopDown.
  }

  @OptIn(ExperimentalContracts::class)
  private fun assertNotNestedRoot(node: MarkdownNode) {
    contract { returns() implies (node is MarkdownNode.Renderable) }
    if (node is MarkdownNode.Root) {
      throw NotImplementedError("Nested markdown is not supported.")
    }
  }

  @OptIn(ExperimentalContracts::class)
  private fun assertNotNestedGroup(node: MarkdownNode) {
    contract { returns() implies (node is MarkdownNode.Text) }
    if (node is MarkdownNode.Group) {
      throw NotImplementedError("Nested group is not supported.")
    }
  }

  private fun notSupportedOperation(operation: String): Nothing =
    throw NotImplementedError(
      "Operation $operation is not supported. Dynamic layouts are not currently " +
        "supported. (Did you try recomposition?)",
    )
}
