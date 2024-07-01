/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/compose-markdown/blob/main/LICENSE
 */

package land.sungbin.markdown.ui

import androidx.compose.runtime.Updater

@PublishedApi
internal data object EmptyUpdater : (Updater<out Any>) -> Unit {
  override fun invoke(updater: Updater<out Any>) {}
}
