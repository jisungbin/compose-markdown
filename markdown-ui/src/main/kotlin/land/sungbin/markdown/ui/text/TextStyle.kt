/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/compose-markdown/blob/main/LICENSE
 */

package land.sungbin.markdown.ui.text

import androidx.compose.runtime.Immutable
import androidx.compose.ui.util.fastFold
import land.sungbin.markdown.runtime.MarkdownOptions

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

  override fun transform(options: MarkdownOptions, value: String): String =
    transformers.fastFold(value) { acc, transformer -> transformer.transform(options, acc) }

  public companion object {
    public val Default: TextStyle = TextStyle()
  }
}

private object UppercaseTransformer : TextTransformer {
  override fun transform(options: MarkdownOptions, value: String): String = value.uppercase()
}

private object LowercaseTransformer : TextTransformer {
  override fun transform(options: MarkdownOptions, value: String): String = value.lowercase()
}
