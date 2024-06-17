package land.sungbin.markdown.ui

import androidx.compose.runtime.Updater
import okio.Buffer

internal val bufferCursor = Buffer.UnsafeCursor()

@PublishedApi
internal data object EmptyUpdater : (Updater<out Any>) -> Unit {
  override fun invoke(updater: Updater<out Any>) {}
}
