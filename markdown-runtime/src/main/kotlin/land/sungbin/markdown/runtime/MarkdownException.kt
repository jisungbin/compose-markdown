/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/compose-markdown/blob/main/LICENSE
 */

package land.sungbin.markdown.runtime

public open class MarkdownException(
  override val message: String? = null,
  override val cause: Throwable? = null,
) : RuntimeException()
