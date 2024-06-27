/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/compose-markdown/blob/main/LICENSE
 */

package land.sungbin.markdown.runtime

import androidx.compose.runtime.collection.MutableVector

public sealed interface MarkdownNode {
  public interface Root : MarkdownNode {
    public val contents: MutableVector<MarkdownSource>
    public val footnotes: MutableVector<MarkdownSource>
  }

  public sealed interface Renderable : MarkdownNode {
    public fun render(): MarkdownSource
  }

  public fun interface Text : Renderable

  public interface Group : Renderable {
    public val size: Int
    public fun insert(index: Int, node: Renderable)
  }

  public interface Quote : Group
  public interface Code : Group
  public interface List : Group
  public interface Footnote : Group
}
