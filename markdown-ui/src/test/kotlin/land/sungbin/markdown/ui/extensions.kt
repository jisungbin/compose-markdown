package land.sungbin.markdown.ui

import land.sungbin.markdown.ui.text.AbstractText
import land.sungbin.markdown.ui.text.TextTransformer

class TextForTest(value: String? = null) : AbstractText() {
  init {
    if (value != null) {
      sink { writeUtf8(value) }
    }
  }
}

fun TextTransformer.transform(value: String) = transform(bufferOf(value))
