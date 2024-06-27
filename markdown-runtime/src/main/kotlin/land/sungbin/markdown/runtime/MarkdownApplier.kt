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
import okio.Buffer

// AbstractApplier uses MutableList, but I want to use MutableVector.
internal class MarkdownApplier(private val root: MarkdownNode.Root) : Applier<MarkdownNode> {
  private val stack = MutableVector<MarkdownNode>(capacity = 10).apply { add(root) }
  override var current: MarkdownNode = root

  private val textBuffer = Buffer()

  constructor(contents: MutableVector<MarkdownSource>, footnotes: MutableVector<MarkdownSource>) :
    this(MarkdownCanvas(contents, footnotes))

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
      is MarkdownNode.Text -> runtimeError { "Text node cannot have children." }
    }
  }

  override fun down(node: MarkdownNode) {
    stack += node
    current = node
  }

  override fun up() {
    runtimeCheck(stack.isNotEmpty()) { "stack is empty. cannot up()" }

    val tail = current
    assertNotNestedRoot(tail)

    stack.removeAt(stack.lastIndex)
    current = stack.last()

    val current = current

    fun addToCurrentDestination(node: MarkdownNode.Renderable) {
      when (current) {
        is MarkdownNode.Root -> {
          if (node is MarkdownNode.Footnote) current.footnotes.add(node.render())
          else current.contents.add(node.render())
        }
        is MarkdownNode.Group -> current.insert(current.size, node)
        is MarkdownNode.Text -> runtimeError { "Text node cannot have children." }
      }
    }

    when (tail) {
      is MarkdownNode.Text -> textBuffer.writeUtf8(tail.render())
      is MarkdownNode.Group -> repeat(tail.size) { addToCurrentDestination(tail) }
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
    runtimeCheck(current is MarkdownNode.Root) { "Unexpected clear() call on non-root node." }
    current.contents.add(MarkdownSource.END_DOCUMENT)
  }

  override fun insertBottomUp(index: Int, instance: MarkdownNode) {
    // Ignored. We use TopDown.
  }

  @OptIn(ExperimentalContracts::class)
  private fun assertNotNestedRoot(node: MarkdownNode) {
    contract { returns() implies (node is MarkdownNode.Renderable) }
    check(node !is MarkdownNode.Root) { "Nested markdown root is not supported." }
  }

  private fun notSupportedOperation(operation: String): Nothing =
    throw NotImplementedError(
      "Dynamic layouts('$operation' operations) are not currently supported. Did you try recomposition?",
    )
}
