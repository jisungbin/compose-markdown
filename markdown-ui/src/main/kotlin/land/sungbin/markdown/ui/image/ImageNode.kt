package land.sungbin.markdown.ui.image

import androidx.compose.runtime.Immutable
import land.sungbin.markdown.runtime.MarkdownNode
import land.sungbin.markdown.runtime.MarkdownOptions
import land.sungbin.markdown.ui.unit.Size
import okio.Buffer
import okio.Source

@Immutable
public class ImageNode(
  private val url: String,
  private val size: Size? = null,
  private val alt: String? = null,
) : MarkdownNode.Text {
  override fun render(options: MarkdownOptions): Source =
    Buffer().apply {
      writeUtf8("<img ")
      writeUtf8("src=\"$url\" ")
      if (this@ImageNode.size != null) {
        val (width, height) = this@ImageNode.size
        writeUtf8("width=\"$width\" height=\"$height\" ")
      }
      if (alt != null) writeUtf8("alt=\"$alt\" ")
      writeUtf8("/>")
    }
}
