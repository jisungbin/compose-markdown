package land.sungbin.markdown.ui.image

import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import land.sungbin.markdown.runtime.MarkdownComposable
import land.sungbin.markdown.ui.modifier.Modifier
import land.sungbin.markdown.ui.text.AbstractText
import land.sungbin.markdown.ui.text.Text
import land.sungbin.markdown.ui.unit.Size

@PublishedApi
internal class ImageText(
  url: String,
  size: Size? = null,
  alt: String? = null,
) : AbstractText() {
  init {
    sink {
      writeUtf8("<img ")
      writeUtf8("src=\"$url\" ")
      if (size != null) {
        val (width, height) = size
        writeUtf8("width=\"$width\" height=\"$height\" ")
      }
      if (alt != null) writeUtf8("alt=\"$alt\" ")
      writeUtf8("/>")
    }
  }
}

@Suppress("NOTHING_TO_INLINE")
@[Composable NonRestartableComposable MarkdownComposable]
public inline fun Image(
  url: String,
  size: Size? = null,
  alt: String? = null,
  modifier: Modifier = Modifier,
) {
  Text(modifier = modifier, value = ImageText(url, size, alt))
}

