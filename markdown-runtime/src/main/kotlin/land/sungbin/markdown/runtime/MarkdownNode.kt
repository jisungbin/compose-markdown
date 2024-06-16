/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/compose-markdown/blob/main/LICENSE
 */

package land.sungbin.markdown.runtime

import okio.Buffer
import okio.Source

public sealed interface MarkdownNode {
  public sealed interface Renderable : MarkdownNode {
    public fun render(options: MarkdownOptions): Source
  }

  public interface Root : MarkdownNode {
    public val options: MarkdownOptions
    public val buffer: Buffer
  }

  public fun interface Text : Renderable

  public interface Group : Renderable {
    // TODO nested groups?
    public fun insert(index: Int, text: Text)
  }
}
