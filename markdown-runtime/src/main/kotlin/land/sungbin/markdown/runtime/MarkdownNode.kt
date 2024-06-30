/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/compose-markdown/blob/main/LICENSE
 */

package land.sungbin.markdown.runtime

import androidx.compose.runtime.collection.MutableVector
import okio.Buffer
import okio.BufferedSource
import okio.Closeable
import org.jetbrains.annotations.TestOnly

// TODO documentation
public class MarkdownNode(
  private val source: ((MarkdownOptions) -> BufferedSource)? = null,
  private val kind: MarkdownKind = MarkdownKind.TEXT,
  private val contentKind: MarkdownKind? = null,
  private val contentTag: ((index: Int, MarkdownOptions) -> String)? = null,
) : Closeable, Cloneable {
  internal val children = MutableVectorWithMutationTracking(MutableVector<MarkdownNode>(capacity = 20)) {
    check(kind.layout) { "Children can be added only to a group or footnote node." }
  }

  internal val text get() = MarkdownKind.TEXT in kind
  internal val group get() = MarkdownKind.GROUP in kind
  internal val footnote get() = MarkdownKind.FOOTNOTE in kind

  @TestOnly internal constructor(
    children: List<MarkdownNode>,
    kind: MarkdownKind = MarkdownKind.GROUP,
    contentKind: MarkdownKind,
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
        if (group) require(contentKind != null) { "A node that is a group must have a 'contentKind'." }
        require(contentTag != null) { "A node that is a group or a footnote must have a 'contentTag'." }
      }
      false -> {
        require(source != null) {
          "A MarkdownNode has been emitted as a MarkdownKind.TEXT, but the 'source' is null. " +
            "Use a 'source' containing a markdown string instead of null."
        }
        require(contentKind == null) { "A node that is not a group or a footnote cannot have a 'contentKind'." }
        require(contentTag == null) { "A node that is not a group or a footnote cannot have a 'contentTag'." }
      }
    }
  }

  internal var index = 0

  private fun tag(index: Int, options: MarkdownOptions): String {
    runtimeCheck(kind.layout) { "A node that is not a group or a footnote cannot have a tag." }
    return contentTag!!.invoke(index, options)
  }

  internal fun draw(options: MarkdownOptions): BufferedSource = Buffer().apply {
    when {
      text -> writeStringMarkdown(options)
      group -> writeGroupMarkdown(options)
      footnote -> writeFootnoteMarkdown(options)
    }
  }

  private fun Buffer.writeStringMarkdown(options: MarkdownOptions) {
    writeAll(source!!.invoke(options))
  }

  private fun Buffer.writeFootnoteMarkdown(options: MarkdownOptions) {
    var tag: String? = null

    children.forEach { child ->
      if (tag == null) tag = tag(child.index, options)
      val source = child.draw(options)
      while (!source.exhausted()) {
        val line = source.readUtf8Line() ?: break
        writeUtf8(tag!!).writeUtf8(line).writeByte(NEW_LINE)
        if (tag != FOOTNOTE_INDENT) tag = FOOTNOTE_INDENT
      }
    }
  }

  private fun Buffer.writeGroupMarkdown(options: MarkdownOptions) {
    children.forEach { child ->
      val source = child.draw(options)
      val tag = tag(child.index, options)
      var touched = false
      while (!source.exhausted()) {
        val line = source.readUtf8Line() ?: break
        val prefix = (contentKind!! + child.kind).prefix(tag = tag, forceConcat = !touched)
        writeUtf8(prefix).writeUtf8(line).writeByte(NEW_LINE)
        touched = true
      }
    }
  }

  private fun MarkdownKind.prefix(tag: String, forceConcat: Boolean): String = when (this) {
    in MarkdownKind.TEXT,
    in MarkdownKind.GROUP,
    -> if (forceConcat || MarkdownKind.REPEATATION_PARENT_TAG in this) tag else " ".repeat(tag.length)
    else -> runtimeError { "[prefix] unreachable code: $this" }
  }

  override fun toString(): String = clone().draw(MarkdownOptions.Default).readUtf8()

  public override fun clone(): MarkdownNode = MarkdownNode(source, kind, contentKind, contentTag).also { clone ->
    children.forEach { child -> clone.children.add(child) }
  }

  override fun close() {
    children.forEach { it.close() }
    children.clear()
  }

  private companion object {
    private const val NEW_LINE = '\n'.code
    private const val FOOTNOTE_INDENT = "    "
  }
}
