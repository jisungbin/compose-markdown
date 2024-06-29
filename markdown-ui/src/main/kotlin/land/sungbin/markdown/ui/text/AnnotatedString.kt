/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/compose-markdown/blob/main/LICENSE
 */

package land.sungbin.markdown.ui.text

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.util.fastFold
import land.sungbin.markdown.runtime.MarkdownOptions
import okio.Buffer

@Immutable
public data class AnnotatedString(public val styledTexts: List<TextAndStyle>) : AbstractText() {
  init {
    val styled = styledTexts.fastFold(Buffer()) { acc, (text, style) ->
      val styled = style.transform(MarkdownOptions.Default, bufferOf(text))
      acc.apply { writeAll(styled.buffer) }
    }
    sink { writeAll(styled) }
  }

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

  private fun bufferOf(value: CharSequence): Buffer {
    if (value is AbstractText) return value.buffer.clone()
    return Buffer().writeUtf8(value.toString())
  }
}

@Stable
public inline fun buildAnnotatedString(builder: AnnotatedString.Builder.() -> Unit): AnnotatedString =
  AnnotatedString.Builder().apply(builder).build()
