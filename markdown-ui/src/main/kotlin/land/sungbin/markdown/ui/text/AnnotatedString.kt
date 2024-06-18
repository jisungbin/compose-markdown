package land.sungbin.markdown.ui.text

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import androidx.compose.ui.util.fastFold
import land.sungbin.markdown.ui.bufferOf
import okio.Buffer

@Immutable
public data class AnnotatedString(public val styledTexts: List<TextAndStyle>) : AbstractText() {
  init {
    val styled = styledTexts.fastFold(Buffer()) { acc, (text, style) ->
      val styled = style.transform(bufferOf(text))
      acc.apply { writeAll(styled.buffer) }
    }
    sink { writeAll(styled) }
  }

  @Immutable
  public data class TextAndStyle(
    public val text: String,
    public val style: TextStyle = TextStyle.Default,
  )

  public class Builder @PublishedApi internal constructor() {
    @PublishedApi
    internal val texts: MutableList<TextAndStyle> = mutableListOf()

    public fun append(text: String) {
      texts.add(TextAndStyle(text))
    }

    public inline fun withStyle(style: TextStyle, crossinline text: () -> String) {
      texts.add(TextAndStyle(text(), style))
    }

    @PublishedApi
    internal fun build(): AnnotatedString = AnnotatedString(texts)
  }
}

@Stable
public inline fun buildAnnotatedString(builder: AnnotatedString.Builder.() -> Unit): AnnotatedString =
  AnnotatedString.Builder().apply(builder).build()
