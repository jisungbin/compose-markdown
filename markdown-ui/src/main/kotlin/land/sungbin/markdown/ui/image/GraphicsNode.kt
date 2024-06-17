package land.sungbin.markdown.ui.image

import androidx.compose.runtime.Immutable
import kotlinx.coroutines.launch
import land.sungbin.markdown.runtime.MarkdownNode
import land.sungbin.markdown.runtime.MarkdownOptions
import land.sungbin.markdown.ui.unit.Size
import okio.Buffer
import okio.FileSystem
import okio.Path
import okio.Source

@Immutable
public class GraphicsNode(
  private val resolver: Resolver,
  private val size: Size? = null,
  private val alt: String? = null,
) : MarkdownNode.Text {
  @Immutable
  public class Image(public val path: Path, public val bytes: ByteArray)

  override fun render(options: MarkdownOptions): Source {
    val image = resolver.resolve(options.defaultImageStorage)

    if (!FileSystem.SYSTEM.exists(image.path)) {
      options.scope.launch {
        FileSystem.SYSTEM.write(image.path, mustCreate = true) { write(image.bytes) }
      }
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

  @Immutable
  public fun interface Resolver {
    public fun resolve(defaultImageStorage: Path?): Image
  }
}
