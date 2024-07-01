/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/compose-markdown/blob/main/LICENSE
 */

package land.sungbin.markdown

import assertk.assertFailure
import assertk.assertions.hasMessage
import assertk.assertions.messageContains
import kotlin.test.Test
import land.sungbin.markdown.runtime.MarkdownApplier
import land.sungbin.markdown.runtime.MarkdownKind
import land.sungbin.markdown.runtime.MarkdownNode

class MarkdownApplierTest {
  @Test fun rootCannotUp() {
    assertFailure { MarkdownApplier().up() }
      .hasMessage("stack is root. cannot up().")
  }

  @Test fun removeIsNotSupported() {
    assertFailure { MarkdownApplier().remove(0, 0) }
      .hasMessage(
        "Dynamic layouts('remove' operations) are not currently supported. " +
          "Try removing recomposition occurrences.",
      )
  }

  @Test fun moveIsNotSupported() {
    assertFailure { MarkdownApplier().move(0, 0, 0) }
      .hasMessage(
        "Dynamic layouts('move' operations) are not currently supported. " +
          "Try removing recomposition occurrences.",
      )
  }

  @Test fun clearCallOnNonRootCausesError() {
    assertFailure {
      MarkdownApplier()
        .apply { down(MarkdownNode(kind = MarkdownKind.GROUP, contentTag = { "" })) }
        .clear()
    }
      .messageContains("Unexpected clear() call on non-root node.")
  }

  @Test fun textNodesCannotHaveChildren() {
    assertFailure {
      MarkdownApplier()
        .apply { down(MarkdownNode(value = "")) }
        .insertTopDown(0, MarkdownNode(value = ""))
    }
      .hasMessage("Cannot add children to a text node.")
  }

  @Test fun nestedFootnotesCauseError() {
    assertFailure {
      MarkdownApplier()
        .apply { down(MarkdownNode(kind = MarkdownKind.FOOTNOTE, contentTag = { "" })) }
        .insertTopDown(0, MarkdownNode(kind = MarkdownKind.FOOTNOTE, contentTag = { "" }))
    }
      .hasMessage("Footnotes cannot be nested.")
  }
}
