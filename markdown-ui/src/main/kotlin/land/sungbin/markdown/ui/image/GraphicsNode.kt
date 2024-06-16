package land.sungbin.markdown.ui.image

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import land.sungbin.markdown.runtime.MarkdownNode
import land.sungbin.markdown.runtime.MarkdownOptions
import land.sungbin.markdown.ui.unit.Size
import okio.Buffer
import okio.FileSystem
import okio.Path
import okio.Source

public class GraphicsNode(
  private val size: Size?,
  private val alt: String?,
  private val resolver: Resolver,
  private val scope: CoroutineScope,
) : MarkdownNode.Text {
  public class Image(public val path: Path, public val bytes: ByteArray)

  override fun render(options: MarkdownOptions): Source {
    val image = resolver.resolve(options.defaultImageStorage)

    scope.launch(Dispatchers.IO) {
      FileSystem.SYSTEM.write(image.path) { write(image.bytes) }
    }

    return Buffer().apply {
      writeUtf8("<img ")
      writeUtf8("src=\"${image.path}\" ")
      if (this@GraphicsNode.size != null) {
        val (width, height) = this@GraphicsNode.size
        writeUtf8("width=\"$width\" height=\"$height\" ")
      }
      if (alt != null) writeUtf8("alt=\"$alt\" ")
      writeUtf8("/>")
    }
  }

  public fun interface Resolver {
    public fun resolve(defaultImageStorage: Path?): Image
  }
}
