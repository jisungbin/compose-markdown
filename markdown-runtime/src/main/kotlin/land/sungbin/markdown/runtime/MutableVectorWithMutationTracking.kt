/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/compose-markdown/blob/main/LICENSE
 */

package land.sungbin.markdown.runtime

import androidx.compose.runtime.collection.MutableVector

/**
 * This class tracks the mutation to the provided [vector] through the provided methods.
 * On mutation, the [onVectorMutated] lambda will be invoked.
 */
// Source: https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:compose/ui/ui/src/commonMain/kotlin/androidx/compose/ui/node/MutableVectorWithMutationTracking.kt;l=25;drc=dcaa116fbfda77e64a319e1668056ce3b032469f
@Suppress("NOTHING_TO_INLINE")
internal class MutableVectorWithMutationTracking<T>(
  internal val vector: MutableVector<T>,
  private val onVectorMutated: () -> Unit,
) {
  inline fun add(element: T) = vector.add(element).also { onVectorMutated() }
}
