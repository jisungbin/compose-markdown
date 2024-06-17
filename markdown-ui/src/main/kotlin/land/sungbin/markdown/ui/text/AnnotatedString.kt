package land.sungbin.markdown.ui.text

import androidx.compose.runtime.Immutable
import androidx.compose.ui.util.fastFold
import land.sungbin.markdown.runtime.MarkdownOptions
import land.sungbin.markdown.ui.bufferOf
import okio.Buffer
import okio.Source

@Immutable
public data class AnnotatedString(public val styledTexts: List<TextAndStyle>) : AbstractText() {
  @Suppress("RedundantVisibilityModifier")
  internal override fun lazyWriting(options: MarkdownOptions): AbstractText =
    apply { write { writeAll(text(options)) } }

  public fun text(options: MarkdownOptions): Source = styledTexts.fastFold(Buffer()) { acc, (text, style) ->
    val styled = style.textTransformer().transform(bufferOf(text), options)
    acc.apply { writeAll(styled.buffer) }
  }

  @Immutable
  public data class TextAndStyle(public val text: String, public val style: TextStyle)
}

// TODO public fun buildAnnotatedString()
