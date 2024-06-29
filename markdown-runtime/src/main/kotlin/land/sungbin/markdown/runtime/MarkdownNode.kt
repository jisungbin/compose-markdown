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
import okio.Source
import okio.Timeout
import okio.buffer

// TODO documentation
public class MarkdownNode(
  private val source: (MarkdownOptions) -> BufferedSource = EMPTY_SOURCE,
  private val kind: MarkdownKind = MarkdownKind.TEXT,
  private val tag: (index: Int, MarkdownOptions) -> String = EMPTY_TAG,
) : Closeable,
  Cloneable {
  private var _source: BufferedSource? = null
  internal val children = MutableVectorWithMutationTracking(MutableVector<BufferedSource>(capacity = 10)) {
    check(group || footnote) { "Children can be added only to a group or footnote node." }
  }

  internal val text get() = MarkdownKind.TEXT in kind
  internal val group get() = MarkdownKind.GROUP in kind
  internal val footnote get() = MarkdownKind.FOOTNOTE in kind

  init {
    if (!group || !footnote) {
      require(source !== EMPTY_SOURCE) {
        "A MarkdownNode has been emitted as a text, but the source is empty. " +
          "Use a source containing a markdown string instead of EMPTY_SOURCE."
      }
    }
  }

  internal var index: Int? = null
  private var cachedTag: String? = null

  internal fun tag(options: MarkdownOptions): String {
    val cached = cachedTag
    if (cached != null) return cached
    val index = checkNotNull(index) { "The tag() was requested before current index was defined." }
    return tag.invoke(index, options).also { cachedTag = it }
  }

  internal fun draw(options: MarkdownOptions? = null, parentTag: String): BufferedSource {
    if (group || footnote) check(source === EMPTY_SOURCE) {
      "A node that is a group or a footnote cannot have its own 'source';" +
        " only children are allowed to be the source of a 'source'."
    }
    if (text) requireNotNull(options) { "Text nodes must have options to draw." }

    val tag = tag(options ?: MarkdownOptions.Default)
    val drawingSource = if (group || footnote) {
      children.vector.fold(Buffer()) { acc, node -> acc.apply { writeAll(node) } }
    } else source(options!!).also { _source = it }

    return Buffer().apply {
      while (!drawingSource.exhausted()) {
        val line = (drawingSource.readUtf8Line() ?: break).let { line ->
          prefix(
            tag = if (!footnote || size == 0L) tag else if (footnote) "    " else runtimeError { "Uncovered case: ${toString()}" },
            concatTag = if (size == 0L || footnote) true else MarkdownKind.REPEATION_TAG in kind,
            consumedTag = MarkdownKind.CONSUMING_TAG in kind && size != 0L,
            parentTag = parentTag,
          )
            .plus(line)
        }
        writeUtf8(line + '\n')
      }
    }
  }

  private fun prefix(tag: String, concatTag: Boolean, consumedTag: Boolean, parentTag: String): String {
    var prefix = if (MarkdownKind.RESPECT_PARENT_TAG in kind) parentTag else ""
    prefix += if (consumedTag) "" else if (concatTag) tag else " ".repeat(tag.length)
    return prefix
  }

  override fun toString(): String =
    clone().apply { index = 0 }
      .draw(options = MarkdownOptions.Default, parentTag = "")
      .readUtf8()

  public override fun clone(): MarkdownNode = MarkdownNode(source, kind, tag).also { clone ->
    children.forEach { child ->
      clone.children.add(child.buffer.clone())
    }
  }

  override fun close() {
    children.forEach { it.close() }
    _source?.close()
    _source = null
  }

  private companion object {
    private val EMPTY_SOURCE: (MarkdownOptions) -> BufferedSource = {
      object : Source {
        override fun read(sink: Buffer, byteCount: Long): Long = -1
        override fun timeout(): Timeout = Timeout.NONE
        override fun close() {}
      }
        .buffer()
    }

    private val EMPTY_TAG: (index: Int, MarkdownOptions) -> String = { _, _ -> "" }
  }
}
