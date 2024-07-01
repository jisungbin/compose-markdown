/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/compose-markdown/blob/main/LICENSE
 */

package land.sungbin.markdown.ui.text

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Immutable
public class AnnotatedString(
  @Suppress("MemberVisibilityCanBePrivate") public val styledTexts: List<TextAndStyle>,
) : CharSequence {
  private val backing = styledTexts.fold(StringBuilder()) { acc, (text, style) ->
    val styled = style.transform(text.toString())
    acc.append(styled)
  }
    .toString()

  override val length: Int = backing.length

  override fun get(index: Int): Char = backing[index]

  override fun subSequence(startIndex: Int, endIndex: Int): CharSequence =
    backing.subSequence(startIndex, endIndex)

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is CharSequence) return false
    return backing == other.toString()
  }

  override fun hashCode(): Int = backing.hashCode()
  override fun toString(): String = backing

  @Immutable
  public data class TextAndStyle(
    public val text: CharSequence,
    public val style: TextStyle = TextStyle.Default,
  )

  public class Builder @PublishedApi internal constructor() {
    @PublishedApi
    internal val texts: MutableList<TextAndStyle> = mutableListOf()

    public fun append(text: CharSequence) {
      texts.add(TextAndStyle(text))
    }

    public inline fun withStyle(style: TextStyle, crossinline text: () -> CharSequence) {
      texts.add(TextAndStyle(text(), style))
    }

    @PublishedApi
    internal fun build(): AnnotatedString = AnnotatedString(texts)
  }
}

@Stable
public inline fun buildAnnotatedString(builder: AnnotatedString.Builder.() -> Unit): AnnotatedString =
  AnnotatedString.Builder().apply(builder).build()
