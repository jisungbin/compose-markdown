package land.sungbin.markdown.ui.text

import androidx.compose.runtime.Immutable
import androidx.compose.ui.util.fastFold
import okio.Buffer
import okio.Source

@Immutable
public data class AnnotatedString(private val textAndStyles: List<Pair<String, TextStyle>>) {
  init {
    require(textAndStyles.isNotEmpty()) { "AnnotatedString should have at least one text and style pair" }
  }

  public fun text(): Source = textAndStyles.fastFold(Buffer()) { acc, (text, style) ->
    acc.apply { writeAll(style.textTransformer().transform(Buffer().writeUtf8(text)).buffer) }
  }
}

// TODO public fun buildAnnotatedString()
