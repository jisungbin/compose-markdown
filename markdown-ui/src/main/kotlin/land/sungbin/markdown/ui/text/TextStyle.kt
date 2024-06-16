package land.sungbin.markdown.ui.text

import androidx.compose.runtime.Immutable
import androidx.compose.ui.util.fastFold
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
) {
  init {
    if (uppercase && lowercase) error("Cannot set uppercase and lowercase at the same time")
  }

  public fun textTransformer(): TextTransformer = TextTransformer { sink ->
    val transformers = buildList {
      if (uppercase) add(UppercaseTransformer)
      if (lowercase) add(LowercaseTransformer)
      if (bold) add(TextStyleDefinition.Bold)
      if (italics) add(TextStyleDefinition.Italic)
      if (strikethrough) add(TextStyleDefinition.Strikethrough)
      if (underline) add(TextStyleDefinition.Unerline)
      if (monospace) add(TextStyleDefinition.Monospace)
    }
    transformers.fastFold(sink) { acc, transformer -> transformer.transform(acc) }
  }

  public companion object {
    public val Default: TextStyle = TextStyle()
  }
}

private object UppercaseTransformer : TextTransformer {
  private const val UPPER_CASE_OFFSET = ('A'.code - 'a'.code).toByte()

  override fun transform(sink: BufferedSink) = sink.apply {
    buffer.readAndWriteUnsafe().use { cursor ->
      cursor.seek(0)
      while (cursor.next() != -1) {
        cursor.data!![cursor.start] = cursor.data!![cursor.start].uppercase()
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

  override fun transform(sink: BufferedSink) = sink.apply {
    buffer.readAndWriteUnsafe().use { cursor ->
      cursor.seek(0)
      while (cursor.next() != -1) {
        cursor.data!![cursor.start] = cursor.data!![cursor.start].lowercase()
      }
    }
  }

  private fun Byte.lowercase(): Byte {
    if (this !in 'A'.code.toByte()..'Z'.code.toByte()) return this
    return (this + LOWER_CASE_OFFSET).toByte()
  }
}
