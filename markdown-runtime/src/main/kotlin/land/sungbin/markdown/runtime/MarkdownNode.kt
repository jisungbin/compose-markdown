/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/compose-markdown/blob/main/LICENSE
 */

package land.sungbin.markdown.runtime

import androidx.compose.runtime.collection.MutableVector
import org.jetbrains.annotations.TestOnly

// TODO documentation
public class MarkdownNode(
  private val value: String? = null,
  private val kind: MarkdownKind = MarkdownKind.TEXT,
  private val contentKind: MarkdownKind = MarkdownKind.ANY,
  private val contentTag: ((index: Int) -> String)? = null,
) {
  internal val children = MutableVectorWithMutationTracking(MutableVector<MarkdownNode>(capacity = 20)) {
    check(kind.layout) { "Children can be added only to a group or footnote node." }
  }

  internal val text get() = MarkdownKind.TEXT in kind
  internal val group get() = MarkdownKind.GROUP in kind
  internal val footnote get() = MarkdownKind.FOOTNOTE in kind

  @TestOnly internal constructor(
    children: List<MarkdownNode>,
    kind: MarkdownKind = MarkdownKind.GROUP,
    contentKind: MarkdownKind = MarkdownKind.ANY,
    contentTag: (index: Int) -> String,
  ) : this(kind = kind, contentKind = contentKind, contentTag = contentTag) {
    for (i in children.indices) {
      this.children.add(children[i])
    }
  }

  init {
    require(text || group || footnote) {
      "A MarkdownNode must have a kind of MarkdownKind.TEXT, MarkdownKind.GROUP, or MarkdownKind.FOOTNOTE."
    }

    when (kind.layout) {
      true -> {
        require(value == null) {
          "A node that is a group or a footnote cannot have its own 'value'. " +
            "Only children are allowed to be the source of a 'value'."
        }
        require(contentTag != null) { "A node that is a group or a footnote must have a 'contentTag'." }
      }
      false -> {
        require(value != null) {
          "A MarkdownNode has been emitted as a MarkdownKind.TEXT, but the 'value' is null. " +
            "Use a 'value' producing a markdown string instead of null."
        }
        require(contentKind == MarkdownKind.ANY) { "A node that is not a group or a footnote cannot have a 'contentKind'." }
        require(contentTag == null) { "A node that is not a group or a footnote cannot have a 'contentTag'." }
      }
    }
  }

  internal var index = 0

  private fun tag(index: Int): String {
    runtimeCheck(kind.layout) { "A node that is not a group or a footnote cannot have a tag." }
    return contentTag!!.invoke(index)
  }

  internal fun draw(): String = when {
    text -> drawText()
    group -> drawGroup()
    footnote -> drawFootnote()
    else -> runtimeError { "[draw] unreachable code: $kind" }
  }

  private fun drawText(): String = value!!

  private fun drawFootnote() = buildString {
    var tag = tag(children.vector.firstOrNull()?.index ?: return@buildString)
    children.vector.forEach { child ->
      val source = child.draw().lineSequence()
      for (line in source) {
        append(tag).append(line).append(NEW_LINE)
        if (tag != FOOTNOTE_INDENT) tag = FOOTNOTE_INDENT
      }
    }
  }

  private fun drawGroup() = buildString {
    val lastChildIndex = children.vector.lastIndex
    children.vector.forEachIndexed { index, child ->
      val tag = tag(child.index)
      var touched = false
      val source = child.draw().lineSequence().iterator()
      while (source.hasNext()) {
        val line = source.next()
        val prefix = (contentKind + child.kind).prefix(tag = tag, forceConcat = !touched)
        append(prefix).append(line)
        if (index != lastChildIndex || source.hasNext()) append(NEW_LINE)
        if (!touched) touched = true
      }
    }
  }

  private fun MarkdownKind.prefix(tag: String, forceConcat: Boolean): String = when (this) {
    in MarkdownKind.TEXT,
    in MarkdownKind.GROUP,
    -> if (forceConcat || MarkdownKind.REPEATATION_PARENT_TAG in this) tag else " ".repeat(tag.length)
    else -> runtimeError { "[prefix] unreachable code: $this" }
  }

  override fun toString(): String = "[MarkdownNode: ${draw()}]"

  private companion object {
    private const val NEW_LINE = '\n'
    private const val FOOTNOTE_INDENT = "    "
  }
}
