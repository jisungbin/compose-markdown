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

  init {
    if (contains(REPEATION_TAG) || contains(RESPECT_PARENT_TAG) || contains(CONSUMING_TAG)) {
      require(contains(GROUP) || contains(FOOTNOTE)) {
        "CONSUMING_TAG, REPEATION_TAG and RESPECT_PARENT_TAG can only be used with GROUP or FOOTNOTE"
      }
    }

    require(!(contains(GROUP) && contains(FOOTNOTE))) {
      "Cannot set GROUP and FOOTNOTE at the same time"
    }

    require(!(contains(CONSUMING_TAG) && contains(REPEATION_TAG))) {
      "Cannot set CONSUMING_TAG and REPEATION_TAG at the same time"
    }
  }

  public companion object {
    public val TEXT: MarkdownKind inline get() = MarkdownKind(0b1 shl 0)
    public val GROUP: MarkdownKind inline get() = MarkdownKind(0b1 shl 1)
    public val FOOTNOTE: MarkdownKind inline get() = MarkdownKind(0b1 shl 2)

    public val CONSUMING_TAG: MarkdownKind inline get() = MarkdownKind(0b1 shl 10)
    public val REPEATION_TAG: MarkdownKind inline get() = MarkdownKind(0b1 shl 11)
    public val RESPECT_PARENT_TAG: MarkdownKind inline get() = MarkdownKind(0b1 shl 12)
  }
}
