/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/compose-markdown/blob/main/LICENSE
 */

package land.sungbin.markdown.runtime

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Composition
import androidx.compose.runtime.withRunningRecomposer
import kotlin.coroutines.coroutineContext
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.withContext
import okio.Buffer

public suspend inline fun Buffer.markdown(
  options: MarkdownOptions = MarkdownOptions.Default,
  crossinline content: @MarkdownComposable @Composable () -> Unit,
) {
  val job = Job(parent = coroutineContext[Job])
  val composeContext = coroutineContext + ImmediatelyFrameClock + job

  withContext(context = composeContext) {
    var composition: Composition? = null
    try {
      withRunningRecomposer { recomposer ->
        composition = Composition(
          applier = MarkdownApplier(options = options, buffer = this@markdown),
          parent = recomposer,
        )
        composition!!.setContent { content() }
      }
    } catch (cce: CancellationException) {
      job.cancel(cce)
    } catch (error: Throwable) {
      job.cancel(CancellationException().apply { initCause(error) })
      throw error
    } finally {
      composition?.dispose()
    }
  }

  job.cancelAndJoin()
}
