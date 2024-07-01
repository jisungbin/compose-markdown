/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/compose-markdown/blob/main/LICENSE
 */

package land.sungbin.markdown

import assertk.assertFailure
import assertk.assertThat
import assertk.assertions.hasMessage
import assertk.assertions.isEqualTo
import kotlin.test.Test
import land.sungbin.markdown.runtime.MarkdownKind
import land.sungbin.markdown.runtime.MarkdownNode

class MarkdownNodeTest {
  private val worldSource = "Hello, World!\nMorning, World!\nBye, World!"
  private val worldSource2 = "Hello, World2!\nMorning, World2!\nBye, World2!"

  private lateinit var node: MarkdownNode

  @Test fun markdownNodeShouldHaveTextOrLayoutKind() {
    assertFailure { node = MarkdownNode(kind = MarkdownKind.REPEATATION_PARENT_TAG) }
      .hasMessage(
        "A MarkdownNode must have a kind of MarkdownKind.TEXT, MarkdownKind.GROUP, " +
          "or MarkdownKind.FOOTNOTE.",
      )
  }

  @Test fun noneLayoutNodeShouldHaveValue() {
    assertFailure { node = MarkdownNode() }
      .hasMessage(
        "A MarkdownNode has been emitted as a MarkdownKind.TEXT, but the 'value' is null. " +
          "Use a 'value' producing a markdown string instead of null.",
      )
  }

  @Test fun noneLayoutNodeShouldntHaveContentKind() {
    assertFailure { node = MarkdownNode(value = "", contentKind = MarkdownKind.TEXT) }
      .hasMessage("A node that is not a group or a footnote cannot have a 'contentKind'.")
  }

  @Test fun noneLayoutNodeShouldntHaveContentTag() {
    assertFailure { node = MarkdownNode(value = "", contentTag = { "" }) }
      .hasMessage("A node that is not a group or a footnote cannot have a 'contentTag'.")
  }

  @Test fun layoutNodeShouldntHaveValue() {
    assertFailure { node = MarkdownNode(value = "", kind = MarkdownKind.GROUP) }
      .hasMessage(
        "A node that is a group or a footnote cannot have its own 'value'. " +
          "Only children are allowed to be the source of a 'value'.",
      )
  }

  @Test fun layoutNodeShouldHaveContentTag() {
    assertFailure { node = MarkdownNode(kind = MarkdownKind.GROUP, contentKind = MarkdownKind.TEXT) }
      .hasMessage("A node that is a group or a footnote must have a 'contentTag'.")
  }

  @Test fun childrenCanBeAddedOnlyToLayoutNode() {
    assertFailure {
      node = MarkdownNode(value = "")
      node.children.add(MarkdownNode(value = ""))
    }
      .hasMessage("Children can be added only to a group or footnote node.")
  }

  @Test fun drawingText() {
    node = MarkdownNode(value = worldSource)

    assertThat(node.draw())
      .isEqualTo("Hello, World!\nMorning, World!\nBye, World!")
  }

  @Test fun drawingFootnoteText() {
    node = MarkdownNode(
      children = listOf(
        MarkdownNode(value = worldSource),
        MarkdownNode(value = worldSource2),
      ),
      kind = MarkdownKind.FOOTNOTE,
      contentTag = { "[^T]: " },
    )

    assertThat(node.draw())
      .isEqualTo(
        "[^T]: Hello, World!\n    Morning, World!\n    Bye, World!\n" +
          "    Hello, World2!\n    Morning, World2!\n    Bye, World2!\n",
      )
  }

  @Test fun drawingGroupInRoot() {
    node = MarkdownNode(
      children = listOf(
        MarkdownNode(value = worldSource),
        MarkdownNode(value = worldSource2),
      ),
      kind = MarkdownKind.GROUP,
      contentKind = MarkdownKind.REPEATATION_PARENT_TAG,
      contentTag = { "P. " },
    )

    assertThat(node.draw())
      .isEqualTo(
        "P. Hello, World!\nP. Morning, World!\nP. Bye, World!\n" +
          "P. Hello, World2!\nP. Morning, World2!\nP. Bye, World2!",
      )
  }

  @Test fun drawingGroupInGroup() {
    val childNode = MarkdownNode(
      children = listOf(MarkdownNode(value = worldSource2)),
      kind = MarkdownKind.GROUP + MarkdownKind.REPEATATION_PARENT_TAG,
      contentKind = MarkdownKind.REPEATATION_PARENT_TAG,
      contentTag = { "- " },
    )
      .apply { index = 1 }

    node = MarkdownNode(
      children = listOf(
        MarkdownNode(value = worldSource),
        childNode,
      ),
      kind = MarkdownKind.GROUP,
      contentKind = MarkdownKind.REPEATATION_PARENT_TAG,
      contentTag = { "> " },
    )

    assertThat(node.draw())
      .isEqualTo(
        """
          > Hello, World!
          > Morning, World!
          > Bye, World!
          > - Hello, World2!
          > - Morning, World2!
          > - Bye, World2!
        """.trimIndent(),
      )
  }

  @Test fun drawingGroupInNestedGroup() {
    val nestedChildrenNodes = MarkdownNode(
      children = List(3) { actualIndex ->
        MarkdownNode(value = "My ordered list!\nMy ordered list's new line!")
          .apply { index = actualIndex }
      },
      kind = MarkdownKind.GROUP + MarkdownKind.REPEATATION_PARENT_TAG,
      contentTag = { index -> "${index + 1}. " },
    )
    val childrenNodes = MarkdownNode(
      children = listOf(
        nestedChildrenNodes,
        MarkdownNode(value = worldSource),
        MarkdownNode(value = worldSource2),
      ),
      kind = MarkdownKind.GROUP,
      contentKind = MarkdownKind.REPEATATION_PARENT_TAG,
      contentTag = { "> " },
    )
    node = MarkdownNode(
      children = listOf(
        MarkdownNode(value = "Hello!"),
        childrenNodes,
      ),
      kind = MarkdownKind.GROUP,
      contentTag = { "" },
    )

    assertThat(node.draw())
      .isEqualTo(
        """
        Hello!
        > 1. My ordered list!
        >    My ordered list's new line!
        > 2. My ordered list!
        >    My ordered list's new line!
        > 3. My ordered list!
        >    My ordered list's new line!
        > Hello, World!
        > Morning, World!
        > Bye, World!
        > Hello, World2!
        > Morning, World2!
        > Bye, World2!
        """.trimIndent(),
      )
  }
}
