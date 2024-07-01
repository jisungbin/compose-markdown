/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/compose-markdown/blob/main/LICENSE
 */

package land.sungbin.markdown.ui.modifier

import androidx.compose.runtime.Stable
import androidx.compose.ui.util.fastFold
import land.sungbin.markdown.runtime.MarkdownOptions
import land.sungbin.markdown.ui.text.TextTransformer

@Stable
public sealed interface Modifier {
  public override fun hashCode(): Int
  public override fun equals(other: Any?): Boolean

  public companion object : Modifier {
    override fun hashCode(): Int = 0
    override fun equals(other: Any?): Boolean = other === Modifier
  }
}

internal class MutableModifier : Modifier {
  val transformers: List<TextTransformer>
    field = ArrayList()

  val footnotes: List<FootnoteGroup>
    field = ArrayList()

  fun add(transformer: TextTransformer) {
    transformers.add(transformer)
  }

  fun add(footnote: FootnoteGroup) {
    footnotes.add(footnote)
  }

  override fun hashCode(): Int {
    var result = transformers.hashCode()
    result = 31 * result + footnotes.hashCode()
    return result
  }

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (javaClass != other?.javaClass) return false

    other as MutableModifier

    if (transformers != other.transformers) return false
    if (footnotes != other.footnotes) return false

    return true
  }
}

@Stable
public infix fun Modifier.then(transformer: TextTransformer): Modifier {
  if (this === Modifier) return MutableModifier().apply { add(transformer) }
  return (this as MutableModifier).apply { add(transformer) }
}

internal infix fun Modifier.with(footnote: FootnoteGroup): Modifier {
  if (this === Modifier) return MutableModifier().apply { add(footnote) }
  return (this as MutableModifier).apply { add(footnote) }
}

internal fun Modifier.applyTo(options: MarkdownOptions, value: String): String {
  if (this !is MutableModifier) return value
  return transformers.fastFold(value) { acc, transformer -> transformer.transform(options, acc) }
}
