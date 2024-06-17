package land.sungbin.markdown.ui.text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import land.sungbin.markdown.runtime.MarkdownComposable
import land.sungbin.markdown.ui.bufferOf
import land.sungbin.markdown.ui.modifier.Modifier
import okio.BufferedSource

@PublishedApi
internal class HeaderText(level: Int, value: BufferedSource) : AbstractText() {
  init {
    require(level in 1..6) { "Header level should be in 1..6" }

    write {
      repeat(level) { writeByte('#'.code) }
      writeByte(' '.code)
      writeAll(value)
    }
  }
}

@Suppress("NOTHING_TO_INLINE")
@[Composable NonRestartableComposable MarkdownComposable]
public inline fun Header(
  level: Int,
  text: CharSequence,
  modifier: Modifier = Modifier,
) {
  Text(
    modifier = modifier,
    value = HeaderText(level, value = bufferOf(text)),
  )
}

@Suppress("NOTHING_TO_INLINE")
@[Composable NonRestartableComposable MarkdownComposable]
public inline fun H1(text: CharSequence, modifier: Modifier = Modifier) {
  Text(
    modifier = modifier,
    value = HeaderText(level = 1, value = bufferOf(text)),
  )
}

@Suppress("NOTHING_TO_INLINE")
@[Composable NonRestartableComposable MarkdownComposable]
public inline fun H2(text: CharSequence, modifier: Modifier = Modifier) {
  Text(
    modifier = modifier,
    value = HeaderText(level = 2, value = bufferOf(text)),
  )
}

@Suppress("NOTHING_TO_INLINE")
@[Composable NonRestartableComposable MarkdownComposable]
public inline fun H3(text: CharSequence, modifier: Modifier = Modifier) {
  Text(
    modifier = modifier,
    value = HeaderText(level = 3, value = bufferOf(text)),
  )
}

@Suppress("NOTHING_TO_INLINE")
@[Composable NonRestartableComposable MarkdownComposable]
public inline fun H4(text: CharSequence, modifier: Modifier = Modifier) {
  Text(
    modifier = modifier,
    value = HeaderText(level = 4, value = bufferOf(text)),
  )
}

@Suppress("NOTHING_TO_INLINE")
@[Composable NonRestartableComposable MarkdownComposable]
public inline fun H5(text: CharSequence, modifier: Modifier = Modifier) {
  Text(
    modifier = modifier,
    value = HeaderText(level = 5, value = bufferOf(text)),
  )
}

@Suppress("NOTHING_TO_INLINE")
@[Composable NonRestartableComposable MarkdownComposable]
public inline fun H6(text: CharSequence, modifier: Modifier = Modifier) {
  Text(
    modifier = modifier,
    value = HeaderText(level = 6, value = bufferOf(text)),
  )
}
