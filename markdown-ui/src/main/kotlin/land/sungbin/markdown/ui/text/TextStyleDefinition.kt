package land.sungbin.markdown.ui.text

import androidx.compose.runtime.Immutable
import land.sungbin.markdown.ui.bufferCursor
import land.sungbin.markdown.ui.fastRepeat
import okio.BufferedSink

@Immutable
public data class TextStyleDefinition(public val open: String, public val end: String = open) : TextTransformer {
  override fun transform(sink: BufferedSink): BufferedSink = sink.apply {
    buffer.readAndWriteUnsafe(bufferCursor).use { cursor ->
      val previousSize = cursor.resizeBuffer(buffer.size + open.length + end.length)
      cursor.seek(0)
      cursor.data!!.copyInto(
        destination = cursor.data!!,
        destinationOffset = open.length,
        startIndex = cursor.start,
        endIndex = previousSize.toInt(),
      )
      fastRepeat(open.length) { offset ->
        cursor.data!![cursor.start + offset] = open[offset].code.toByte()
      }
      cursor.seek(cursor.buffer!!.size - end.length)
      fastRepeat(end.length) { offset ->
        cursor.data!![cursor.start + offset] = end[offset].code.toByte()
      }
    }
  }

  public companion object {
    public val Bold: TextStyleDefinition = TextStyleDefinition("**")
    public val Italic: TextStyleDefinition = TextStyleDefinition("_")
    public val Strikethrough: TextStyleDefinition = TextStyleDefinition("~~")
    public val Unerline: TextStyleDefinition = TextStyleDefinition("<ins>", "</ins>")
    public val Monospace: TextStyleDefinition = TextStyleDefinition("`")
  }
}
