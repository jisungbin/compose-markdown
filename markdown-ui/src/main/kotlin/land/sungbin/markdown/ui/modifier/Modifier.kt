package land.sungbin.markdown.ui.modifier

import androidx.compose.runtime.Stable
import land.sungbin.markdown.runtime.MarkdownOptions
import land.sungbin.markdown.ui.text.TextTransformer
import okio.BufferedSink

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

private class MutableModifier : Modifier, AbstractMutableCollection<TextTransformer>() {
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

// TODO sink를 clone 하지 않음 -> 문서화 필요
internal fun Modifier.applyTo(sink: BufferedSink, options: MarkdownOptions): BufferedSink {
  if (isEmpty()) return sink
  var acc = sink
  repeat(size) { index -> acc = get(index).transform(acc, options) }
  return acc
}
