/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/compose-markdown/blob/main/LICENSE
 */

package land.sungbin.markdown.ui.modifier

import assertk.assertThat
import assertk.assertions.containsExactly
import assertk.assertions.isSameInstanceAs
import kotlin.test.Test
import land.sungbin.markdown.ui.text.TextTransformer

class ModifierTest {
  @Test fun modifierChaining() {
    val modifier = Modifier
      .then(testModifier1)
      .then(testModifier2)
      .then(testModifier3)
      .then(testModifier4)

    assertThat(modifier.toList()).containsExactly(
      testModifier1,
      testModifier2,
      testModifier3,
      testModifier4,
    )
  }

  @Test fun multiModifierChaining() {
    val modifierChain1 = Modifier
      .then(testModifier1)
      .then(testModifier2)
      .then(testModifier3)
      .then(testModifier4)
    val modifierChain2 = Modifier
      .then(testModifier4)
      .then(testModifier1)
      .then(testModifier3)
      .then(testModifier2)

    assertThat(modifierChain1.toList()).containsExactly(
      testModifier1,
      testModifier2,
      testModifier3,
      testModifier4,
    )
    assertThat(modifierChain2.toList()).containsExactly(
      testModifier4,
      testModifier1,
      testModifier3,
      testModifier2,
    )
  }

  @Test fun emptyModifierIsSame() {
    assertThat(Modifier).isSameInstanceAs(Modifier)
  }

  private val testModifier1 = TextTransformer { _, sink -> sink }
  private val testModifier2 = TextTransformer { _, sink -> sink }
  private val testModifier3 = TextTransformer { _, sink -> sink }
  private val testModifier4 = TextTransformer { _, sink -> sink }
}
