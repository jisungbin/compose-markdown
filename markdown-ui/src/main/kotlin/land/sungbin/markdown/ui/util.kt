package land.sungbin.markdown.ui

import land.sungbin.markdown.ui.text.AbstractText
import okio.Buffer

@PublishedApi
internal fun bufferOf(value: CharSequence): Buffer {
  if (value is AbstractText) return value.buffer.clone()
  return Buffer().writeUtf8(value.toString())
}
