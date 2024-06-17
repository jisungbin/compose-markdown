package land.sungbin.markdown.ui

import land.sungbin.markdown.ui.text.AbstractText
import okio.Buffer

@Suppress("NOTHING_TO_INLINE")
@PublishedApi
internal inline fun bufferOf(value: CharSequence): Buffer {
  if (value is AbstractText) return value.buffer.clone()
  return Buffer().apply {
    repeat(value.length) { index -> writeByte(value[index].code) }
  }
}
