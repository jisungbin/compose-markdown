/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/compose-markdown/blob/main/LICENSE
 */

@file:Suppress("NOTHING_TO_INLINE")

package land.sungbin.markdown.runtime

@JvmInline
public value class MarkdownKind @PublishedApi internal constructor(@PublishedApi internal val mask: Int) {
  public inline operator fun plus(other: MarkdownKind): MarkdownKind = MarkdownKind(mask or other.mask)
  public inline operator fun contains(value: MarkdownKind): Boolean = (mask and value.mask) != 0

  public val layout: Boolean inline get() = GROUP in this || FOOTNOTE in this

  public companion object {
    public val ANY: MarkdownKind inline get() = MarkdownKind(0b1 shl 0)
    public val TEXT: MarkdownKind inline get() = MarkdownKind(0b1 shl 1)

    public val GROUP: MarkdownKind inline get() = MarkdownKind(0b1 shl 5)
    public val FOOTNOTE: MarkdownKind inline get() = MarkdownKind(0b1 shl 6)

    public val REPEATATION_PARENT_TAG: MarkdownKind inline get() = MarkdownKind(0b1 shl 10)
  }
}
