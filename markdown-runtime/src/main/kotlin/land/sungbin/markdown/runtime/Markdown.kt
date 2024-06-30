/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/compose-markdown/blob/main/LICENSE
 */

package land.sungbin.markdown.runtime

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Composition
import androidx.compose.runtime.CompositionLocalContext
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.withRunningRecomposer
import kotlin.coroutines.coroutineContext
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

@Suppress("NOTHING_TO_INLINE")
@Composable
public inline fun markdown(
  options: MarkdownOptions = MarkdownOptions.Default,
  noinline content: @MarkdownComposable @Composable () -> Unit,
): String {
  val scope = rememberCoroutineScope()
  val locals = currentCompositionLocalContext
  return runBlocking(scope.coroutineContext) {
    markdown(options = options, parentLocals = locals, content = content)
  }
}

public suspend fun markdown(
  options: MarkdownOptions = MarkdownOptions.Default,
  parentLocals: CompositionLocalContext? = null,
  content: @MarkdownComposable @Composable () -> Unit,
): String {
  val job = Job(parent = coroutineContext[Job])
  val composeContext = coroutineContext + ImmediatelyFrameClock + job
  val root = MarkdownNode(kind = MarkdownKind.GROUP)
  val footnotes = MarkdownNode(kind = MarkdownKind.GROUP)

  withContext(context = composeContext) {
    var composition: Composition? = null
    try {
      withRunningRecomposer { recomposer ->
        composition = Composition(MarkdownApplier(root, footnotes), parent = recomposer).apply {
          setContent {
            when (parentLocals) {
              null -> content()
              else -> CompositionLocalProvider(parentLocals) { content() }
            }
          }
        }
      }
    } catch (cce: CancellationException) {
      job.cancel(cce)
    } catch (error: Throwable) {
      job.cancel(CancellationException().apply { initCause(error) })
    } finally {
      composition?.dispose()
    }
  }
  job.cancelAndJoin()

  return buildString {
    appendLine(root.draw(options))
    append(footnotes.draw(options))
  }.also {
    root.close()
    footnotes.close()
  }
}
