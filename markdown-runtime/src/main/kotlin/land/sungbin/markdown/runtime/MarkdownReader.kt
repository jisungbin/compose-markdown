package land.sungbin.markdown.runtime

import androidx.compose.runtime.collection.mutableVectorOf
import okio.BufferedSource

public interface MarkdownReader : AutoCloseable {
  public fun readLine(): String
  public fun peek(): MarkdownToken
}

public class RealMarkdownReader(private val buffer: List<MarkdownSource>) : MarkdownReader {
  private var openQuote = 0
  private var openCode = 0
  private var openFootnote = 0
  private var openList = 0

  private var current = -1
  private val root = mutableVectorOf<MarkdownToken>()

  override fun readLine(): String {
    val token = peek().also { current++ }
    val source = lazy { buffer[current].buffer }

    return when (token) {
      is MarkdownToken.Text -> {
        source.value.readUtf8()
      }
      is MarkdownToken.NewLine -> {
        ""
      }
      is MarkdownToken.BeginQuote -> {
        quote(++openQuote, source.value)
      }
      is MarkdownToken.EndQuote -> run { openQuote--; "" }
      is MarkdownToken.BeginCode -> run { openCode++; code(token.language, source.value) }
      is MarkdownToken.EndCode -> run { openCode--; "" }
      is MarkdownToken.BeginFootnote -> footnote(token.tag, source.value)
      is MarkdownToken.EndFootnote -> run { openFootnote--; "" }
      is MarkdownToken.BeginList -> list(++openList, source.value)
      is MarkdownToken.EndList -> run { openList--; "" }
      is MarkdownToken.EndDocument -> error("End of document")
    }.also {
      if (source.isInitialized()) source.value.close()
      buffer[current].close()
    }
  }

  override fun peek(): MarkdownToken = buffer[current + 1].token

  override fun close() {
    runtimeCheck(openQuote == 0) { "Unbalanced quote (openQuote=$openQuote)" }
    runtimeCheck(openCode == 0) { "Unbalanced code (openCode=$openCode)" }
    runtimeCheck(openFootnote == 0) { "Unbalanced footnote (openFootnote=$openFootnote)" }
    runtimeCheck(openList == 0) { "Unbalanced list (openList=$openList)" }
  }

  private fun indent() {
    TODO()
  }

  private fun quote(level: Int, source: BufferedSource) = buildString {
    val prefix = "> ".repeat(level)
    if (level - 1 > 0) appendLine(prefix.dropLast(2))
    while (!source.exhausted()) {
      append(prefix)
      appendLine(source.readUtf8Line())
    }
  }

  private fun code(language: String, source: BufferedSource) = buildString {
    appendLine("```$language")
    appendLine(source.readUtf8())
    append("```")
  }

  private fun footnote(tag: String, source: BufferedSource): String {
    TODO()
  }

  private fun list(level: Int, source: BufferedSource): String {
    TODO()
  }

  private inline fun runtimeCheck(condition: Boolean, message: () -> String) {
    if (!condition) error(
      "Problem with compose-markdown runtime. Please check the GitHub issue for the same issue, " +
        "or register a new one. (https://github.com/jisungbin/compose-markdown/issues)\n " +
        message()
    )
  }
}
