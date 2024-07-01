/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/compose-markdown/blob/main/LICENSE
 */

package land.sungbin.markdown.ui

import land.sungbin.markdown.runtime.MarkdownOptions
import land.sungbin.markdown.ui.text.TextTransformer

fun TextTransformer.transform(value: String) = transform(MarkdownOptions.Default, value)
