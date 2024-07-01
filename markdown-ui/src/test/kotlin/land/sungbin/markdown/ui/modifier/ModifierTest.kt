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
  private val testTransformer = TextTransformer { it }
  private val testTransformer2 = TextTransformer { it }
  private val testTransformer3 = TextTransformer { it }
  private val testTransformer4 = TextTransformer { it }

  private val testFootnote = FootnoteGroup(tag = "1", content = {})
  private val testFootnote2 = FootnoteGroup(tag = "2", content = {})
  private val testFootnote3 = FootnoteGroup(tag = "3", content = {})
  private val testFootnote4 = FootnoteGroup(tag = "4", content = {})

  @Test fun transformerChaining() {
    val modifier = Modifier
      .then(testTransformer)
      .then(testTransformer2)
      .then(testTransformer3)
      .then(testTransformer4)

    assertThat(modifier.transformers()).containsExactly(
      testTransformer,
      testTransformer2,
      testTransformer3,
      testTransformer4,
    )
  }

  @Test fun multiTransformerChaining() {
    val modifierChain1 = Modifier
      .then(testTransformer)
      .then(testTransformer2)
      .then(testTransformer3)
      .then(testTransformer4)
    val modifierChain2 = Modifier
      .then(testTransformer4)
      .then(testTransformer)
      .then(testTransformer3)
      .then(testTransformer2)

    assertThat(modifierChain1.transformers()).containsExactly(
      testTransformer,
      testTransformer2,
      testTransformer3,
      testTransformer4,
    )
    assertThat(modifierChain2.transformers()).containsExactly(
      testTransformer4,
      testTransformer,
      testTransformer3,
      testTransformer2,
    )
  }

  @Test fun footnoteChaining() {
    val modifier = Modifier
      .with(testFootnote)
      .with(testFootnote2)
      .with(testFootnote3)
      .with(testFootnote4)

    assertThat(modifier.footnotes()).containsExactly(
      testFootnote,
      testFootnote2,
      testFootnote3,
      testFootnote4,
    )
  }

  @Test fun multiFootnoteChaining() {
    val modifierChain1 = Modifier
      .with(testFootnote)
      .with(testFootnote2)
      .with(testFootnote3)
      .with(testFootnote4)
    val modifierChain2 = Modifier
      .with(testFootnote4)
      .with(testFootnote)
      .with(testFootnote3)
      .with(testFootnote2)

    assertThat(modifierChain1.footnotes()).containsExactly(
      testFootnote,
      testFootnote2,
      testFootnote3,
      testFootnote4,
    )
    assertThat(modifierChain2.footnotes()).containsExactly(
      testFootnote4,
      testFootnote,
      testFootnote3,
      testFootnote2,
    )
  }

  @Test fun mixedChaining() {
    val modifier = Modifier
      .then(testTransformer)
      .with(testFootnote)
      .then(testTransformer2)
      .with(testFootnote2)
      .then(testTransformer3)
      .with(testFootnote3)
      .then(testTransformer4)
      .with(testFootnote4)

    assertThat(modifier.transformers()).containsExactly(
      testTransformer,
      testTransformer2,
      testTransformer3,
      testTransformer4,
    )
    assertThat(modifier.footnotes()).containsExactly(
      testFootnote,
      testFootnote2,
      testFootnote3,
      testFootnote4,
    )
  }

  @Test fun emptyModifierIsSame() {
    assertThat(Modifier).isSameInstanceAs(Modifier)
  }
}

private fun Modifier.transformers() = (this as MutableModifier).transformers
private fun Modifier.footnotes() = (this as MutableModifier).footnotes
