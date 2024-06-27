package land.sungbin.markdown.runtime

public open class MarkdownException(
  override val message: String? = null,
  override val cause: Throwable? = null,
) : RuntimeException()
