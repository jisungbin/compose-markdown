/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/compose-markdown/blob/main/LICENSE
 */

package land.sungbin.markdown.ui.text

import androidx.compose.runtime.Immutable

@Immutable
public data class TextStyleDefinition(public val open: String, public val end: String = open) : TextTransformer {
  override fun transform(value: String): String =
    value.toCharArray(
      destination = CharArray(open.length + value.length + end.length),
      destinationOffset = open.length,
    )
      .also { new ->
        repeat(open.length) { new[it] = open[it] }
        (open.length + value.length).let { pos -> repeat(end.length) { new[pos + it] = end[it] } }
      }
      .concatToString()

  public companion object {
    public val Bold: TextStyleDefinition = TextStyleDefinition("**")
    public val Italic: TextStyleDefinition = TextStyleDefinition("_")
    public val Strikethrough: TextStyleDefinition = TextStyleDefinition("~~")
    public val Unerline: TextStyleDefinition = TextStyleDefinition("<ins>", "</ins>")
    public val Monospace: TextStyleDefinition = TextStyleDefinition("`")
  }
}
