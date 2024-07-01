/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/compose-markdown/blob/main/LICENSE
 */

package land.sungbin.markdown.ui.modifier

import androidx.compose.runtime.Stable
import land.sungbin.markdown.runtime.MarkdownOptions
import land.sungbin.markdown.ui.text.TextTransformer

@Stable
public sealed interface Modifier : Collection<TextTransformer>, RandomAccess {
  public operator fun get(index: Int): TextTransformer
  public override fun hashCode(): Int
  public override fun equals(other: Any?): Boolean

  public companion object : Modifier, List<TextTransformer> by emptyList() {
    override fun hashCode(): Int = 0
    override fun equals(other: Any?): Boolean = other === Modifier
  }
}

private class MutableModifier : AbstractMutableCollection<TextTransformer>(), Modifier {
  private val transformers = ArrayList<TextTransformer>()

  override fun add(element: TextTransformer): Boolean = transformers.add(element)
  override fun get(index: Int): TextTransformer = transformers[index]

  override val size: Int get() = transformers.size
  override fun iterator(): MutableIterator<TextTransformer> = transformers.iterator()

  override fun hashCode(): Int = transformers.hashCode()
  override fun equals(other: Any?): Boolean = transformers == other
}

@Stable
public infix fun Modifier.then(transformer: TextTransformer): Modifier {
  if (this === Modifier) return MutableModifier().apply { add(transformer) }
  return (this as MutableModifier).apply { add(transformer) }
}

// TODO not cloning the sink -> Documentation required
@PublishedApi
internal fun Modifier.applyTo(options: MarkdownOptions, value: String): String {
  if (isEmpty()) return value
  var acc = value
  repeat(size) { index -> acc = get(index).transform(options, acc) }
  return acc
}
