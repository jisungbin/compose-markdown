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
  private val source: ((MarkdownOptions) -> String)? = null,
  private val kind: MarkdownKind = MarkdownKind.TEXT,
  private val contentKind: MarkdownKind = MarkdownKind.ANY,
  private val contentTag: ((index: Int, MarkdownOptions) -> String)? = null,
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
    contentTag: (index: Int, MarkdownOptions) -> String,
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
        require(source == null) {
          "A node that is a group or a footnote cannot have its own 'source'. " +
            "Only children are allowed to be the source of a 'source'."
        }
        require(contentTag != null) { "A node that is a group or a footnote must have a 'contentTag'." }
      }
      false -> {
        require(source != null) {
          "A MarkdownNode has been emitted as a MarkdownKind.TEXT, but the 'source' is null. " +
            "Use a 'source' containing a markdown string instead of null."
        }
        require(contentKind == MarkdownKind.ANY) { "A node that is not a group or a footnote cannot have a 'contentKind'." }
        require(contentTag == null) { "A node that is not a group or a footnote cannot have a 'contentTag'." }
      }
    }
  }

  internal var index = 0

  private fun tag(index: Int, options: MarkdownOptions): String {
    runtimeCheck(kind.layout) { "A node that is not a group or a footnote cannot have a tag." }
    return contentTag!!.invoke(index, options)
  }

  internal fun draw(options: MarkdownOptions): String = when {
    text -> drawStringMarkdown(options)
    group -> drawGroupMarkdown(options)
    footnote -> drawFootnoteMarkdown(options)
    else -> runtimeError { "[draw] unreachable code: $kind" }
  }

  private fun drawStringMarkdown(options: MarkdownOptions): String =
    source!!.invoke(options)

  private fun drawFootnoteMarkdown(options: MarkdownOptions) = buildString {
    var tag = tag(children.vector.firstOrNull()?.index ?: return@buildString, options)
    children.vector.forEach { child ->
      val source = child.draw(options).lineSequence()
      for (line in source) {
        append(tag).append(line).append(NEW_LINE)
        if (tag != FOOTNOTE_INDENT) tag = FOOTNOTE_INDENT
      }
    }
  }

  private fun drawGroupMarkdown(options: MarkdownOptions) = buildString {
    val lastChildIndex = children.vector.lastIndex
    children.vector.forEachIndexed { index, child ->
      val tag = tag(child.index, options)
      var touched = false
      val source = child.draw(options).lineSequence().iterator()
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

  override fun toString(): String = draw(MarkdownOptions.Default)

  private companion object {
    private const val NEW_LINE = '\n'
    private const val FOOTNOTE_INDENT = "    "
  }
}
