package land.sungbin.markdown.ui.text

import androidx.compose.runtime.Immutable
import androidx.compose.ui.util.fastFold
import land.sungbin.markdown.runtime.MarkdownOptions
import land.sungbin.markdown.ui.bufferCursor
import land.sungbin.markdown.ui.fastRepeat
import okio.BufferedSink

@Immutable
public data class TextStyle(
  public val bold: Boolean = false,
  public val italics: Boolean = false,
  public val strikethrough: Boolean = false,
  public val underline: Boolean = false,
  public val monospace: Boolean = false,
  public val uppercase: Boolean = false,
  public val lowercase: Boolean = false,
) : TextTransformer {
  init {
    if (uppercase && lowercase) error("Cannot set uppercase and lowercase at the same time")
  }

  private val transformers = buildList {
    if (uppercase) add(UppercaseTransformer)
    if (lowercase) add(LowercaseTransformer)
    if (bold) add(TextStyleDefinition.Bold)
    if (italics) add(TextStyleDefinition.Italic)
    if (strikethrough) add(TextStyleDefinition.Strikethrough)
    if (underline) add(TextStyleDefinition.Unerline)
    if (monospace) add(TextStyleDefinition.Monospace)
  }

  override fun transform(sink: BufferedSink, options: MarkdownOptions): BufferedSink =
    transformers.fastFold(sink) { acc, transformer -> transformer.transform(acc, options) }

  public companion object {
    public val Default: TextStyle = TextStyle()
  }
}

private object UppercaseTransformer : TextTransformer {
  private const val UPPER_CASE_OFFSET = ('A'.code - 'a'.code).toByte()

  override fun transform(sink: BufferedSink, options: MarkdownOptions): BufferedSink = sink.apply {
    buffer.readAndWriteUnsafe(bufferCursor).use { cursor ->
      cursor.seek(0)
      fastRepeat(cursor.end) { offset ->
        cursor.data!![offset] = cursor.data!![offset].uppercase()
      }
    }
  }

  private fun Byte.uppercase(): Byte {
    if (this !in 'a'.code.toByte()..'z'.code.toByte()) return this
    return (this + UPPER_CASE_OFFSET).toByte()
  }
}

private object LowercaseTransformer : TextTransformer {
  private const val LOWER_CASE_OFFSET = ('a'.code - 'A'.code).toByte()

  override fun transform(sink: BufferedSink, options: MarkdownOptions): BufferedSink = sink.apply {
    buffer.readAndWriteUnsafe(bufferCursor).use { cursor ->
      cursor.seek(0)
      fastRepeat(cursor.end) { offset ->
        cursor.data!![offset] = cursor.data!![offset].lowercase()
      }
    }
  }

  private fun Byte.lowercase(): Byte {
    if (this !in 'A'.code.toByte()..'Z'.code.toByte()) return this
    return (this + LOWER_CASE_OFFSET).toByte()
  }
}
