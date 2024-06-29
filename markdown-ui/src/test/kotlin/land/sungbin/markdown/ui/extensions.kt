/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/compose-markdown/blob/main/LICENSE
 */

package land.sungbin.markdown.ui

import land.sungbin.markdown.runtime.MarkdownOptions
import land.sungbin.markdown.ui.text.AbstractText
import land.sungbin.markdown.ui.text.TextTransformer
import okio.Buffer

class TextForTest(value: String? = null) : AbstractText() {
  init {
    if (value != null) {
      sink { writeUtf8(value) }
    }
  }
}

fun TextTransformer.transform(value: String) = transform(MarkdownOptions.Default, Buffer().apply { writeUtf8(value) })
