package land.sungbin.markdown.runtime

import androidx.compose.runtime.collection.MutableVector
import androidx.compose.runtime.collection.mutableVectorOf
import java.io.Closeable
import land.sungbin.markdown.runtime.MarkdownToken.BeginList.Type
import okio.Buffer
import okio.BufferedSource
import okio.buffer

public class MarkdownRender(
  private val options: MarkdownOptions,
  private val contents: List<MarkdownSource>,
  @Suppress("unused") private val footnotes: List<MarkdownSource>, // TODO
) : Closeable {
  public var current: Int = 0
    private set
  public val size: Int = contents.size

  private val stack = mutableVectorOf<MarkdownToken.Begin>()
  private val prefixes = MutableVector<String>(capacity = 10)

  private val orderedListIndex: Int
    get() = stack.count { it is MarkdownToken.BeginList && it.type == Type.Ordered }

  private val orderedListSize = IntArray(10) { 1 }

  public fun readLineOrNull(): String? {
    val source = contents[current++]
    val buffered = source.buffer()

    return when (val token = source.token) {
      is MarkdownToken.Text -> markdown(buffered)
      is MarkdownToken.NewLine -> markdown(EMPTY_BUFFER)

      is MarkdownToken.BeginQuote,
      is MarkdownToken.BeginCode,
      is MarkdownToken.BeginList,
      -> {
        begin(token as MarkdownToken.Begin)
        null
      }

      is MarkdownToken.EndQuote,
      is MarkdownToken.EndCode,
      is MarkdownToken.EndList,
      -> {
        if (token is MarkdownToken.EndList) orderedListSize[orderedListIndex] = 1
        end(token as MarkdownToken.End)
      }

      is MarkdownToken.BeginFootnote, is MarkdownToken.EndFootnote,
      -> runtimeError { "Footnote tokens were not handled by the Applier" }

      is MarkdownToken.EndDocument -> footnotes()
    }.also {
      buffered.close()
      source.close()
    }
  }

  private fun begin(token: MarkdownToken.Begin) {
    stack += token
    prefixes += when (token) {
      is MarkdownToken.BeginQuote -> "> "
      is MarkdownToken.BeginCode -> "```${token.language ?: options.defaultLanguage}"
      is MarkdownToken.BeginList -> when (token.type) {
        Type.Ordered -> "${orderedListSize[orderedListIndex]++}. "
        Type.Unordered -> "* "
      }
      is MarkdownToken.BeginFootnote -> error("unreachable branch")
    }
  }

  private fun end(token: MarkdownToken.End): String? {
    val validation: (MarkdownToken.Begin) -> Pair<Boolean, String> = {
      when (token) {
        is MarkdownToken.EndQuote -> (it is MarkdownToken.BeginQuote) to "BeginQuote"
        is MarkdownToken.EndCode -> (it is MarkdownToken.BeginCode) to "BeginCode"
        is MarkdownToken.EndList -> (it is MarkdownToken.BeginList) to "BeginList"
        MarkdownToken.EndFootnote -> error("unreachable branch")
      }
    }

    stack.removeLast(validation)
    prefixes.removeLast()

    return if (token is MarkdownToken.EndCode) "```" else null
  }

  private fun markdown(source: BufferedSource): String = buildString {
    var phase = 0
    var prefix = prefixes.joinToString()
    while (!source.exhausted()) {
      val line = source.readUtf8LineStrict()
      when (phase++) {
        0 -> append(prefix).append(line)
        1 -> {
          prefix = when (stack.last()) {
            is MarkdownToken.BeginQuote -> prefix
            is MarkdownToken.BeginCode -> prefixes.copyAndDropLast(1).fastJoinToString()
            is MarkdownToken.BeginList -> run {
              prefixes.copyAndDropLast(1)
                .apply { add("${" ".repeat(orderedListSize[orderedListIndex].toString().length)} ") }
                .fastJoinToString()
            }
            is MarkdownToken.BeginFootnote -> error("unreachable branch")
          }
          append('\n').append(prefix).append(line)
        }
        else -> append('\n').append(prefix).append(line)
      }
    }
  }

  private fun footnotes(): String {
    orderedListSize.fill(1)
    TODO("Not yet implemented: footnotes")
  }

  override fun close() {
    runtimeCheck(stack.isEmpty()) { "Unbalanced stack (stack=$stack)" }
  }

  private companion object {
    private val EMPTY_BUFFER: BufferedSource = Buffer().writeUtf8("")
  }
}

private inline fun <T> MutableVector<T>.removeLast(validation: (T) -> Pair<Boolean, String> = { true to "" }) {
  val removed = removeAt(lastIndex)
  val (success, expect) = validation(removed)
  runtimeCheck(success) {
    "Open and closed nodes do not match. (expect=$expect, actual=$removed)"
  }
}

private inline fun <T> MutableVector<T>.count(predicate: (T) -> Boolean): Int {
  var count = 0
  forEach { if (predicate(it)) count++ }
  return count
}

private fun <T> MutableVector<T>.copyAndDropLast(dropCount: Int): MutableList<T> =
  ArrayList(asMutableList()).apply {
    var lastIndex = lastIndex
    repeat(dropCount) { removeAt(lastIndex--) }
  }

private fun List<*>.fastJoinToString(separator: CharSequence = ""): String {
  val size = size
  return buildString {
    for (index in 0 until size) {
      if (index > 0) append(separator)
      append(get(index))
    }
  }
}

private fun MutableVector<*>.joinToString(separator: CharSequence = ""): String =
  this
    .fold(StringBuilder()) { acc, item ->
      if (acc.isNotEmpty()) acc.append(separator)
      acc.append(item)
    }
    .toString()
