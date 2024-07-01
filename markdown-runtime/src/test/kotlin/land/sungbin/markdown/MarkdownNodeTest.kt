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
import land.sungbin.markdown.runtime.MarkdownOptions

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

  @Test fun noneLayoutNodeShouldHaveFilledSource() {
    assertFailure { node = MarkdownNode() }
      .hasMessage(
        "A MarkdownNode has been emitted as a MarkdownKind.TEXT, but the 'source' is null. " +
          "Use a 'source' containing a markdown string instead of null.",
      )
  }

  @Test fun noneLayoutNodeShouldntHaveContentKind() {
    assertFailure { node = MarkdownNode(source = { "" }, contentKind = MarkdownKind.TEXT) }
      .hasMessage("A node that is not a group or a footnote cannot have a 'contentKind'.")
  }

  @Test fun noneLayoutNodeShouldntHaveContentTag() {
    assertFailure { node = MarkdownNode(source = { "" }, contentTag = { _, _ -> "" }) }
      .hasMessage("A node that is not a group or a footnote cannot have a 'contentTag'.")
  }

  @Test fun layoutNodeShouldntHaveSource() {
    assertFailure { node = MarkdownNode(source = { "" }, kind = MarkdownKind.GROUP) }
      .hasMessage(
        "A node that is a group or a footnote cannot have its own 'source'. " +
          "Only children are allowed to be the source of a 'source'.",
      )
  }

  @Test fun layoutNodeShouldHaveContentTag() {
    assertFailure { node = MarkdownNode(kind = MarkdownKind.GROUP, contentKind = MarkdownKind.TEXT) }
      .hasMessage("A node that is a group or a footnote must have a 'contentTag'.")
  }

  @Test fun childrenCanBeAddedOnlyToLayoutNode() {
    assertFailure {
      node = MarkdownNode(source = { "" })
      node.children.add(MarkdownNode(source = { "" }))
    }
      .hasMessage("Children can be added only to a group or footnote node.")
  }

  @Test fun drawingText() {
    node = MarkdownNode(source = { worldSource })

    assertThat(node.draw(MarkdownOptions()))
      .isEqualTo("Hello, World!\nMorning, World!\nBye, World!")
  }

  @Test fun drawingFootnoteText() {
    node = MarkdownNode(
      children = listOf(
        MarkdownNode(source = { worldSource }),
        MarkdownNode(source = { worldSource2 }),
      ),
      kind = MarkdownKind.FOOTNOTE,
      contentTag = { _, _ -> "[^T]: " },
    )

    assertThat(node.draw(MarkdownOptions()))
      .isEqualTo(
        "[^T]: Hello, World!\n    Morning, World!\n    Bye, World!\n" +
          "    Hello, World2!\n    Morning, World2!\n    Bye, World2!\n",
      )
  }

  @Test fun drawingGroupInRoot() {
    node = MarkdownNode(
      children = listOf(
        MarkdownNode(source = { worldSource }, kind = MarkdownKind.TEXT),
        MarkdownNode(source = { worldSource2 }, kind = MarkdownKind.TEXT),
      ),
      kind = MarkdownKind.GROUP,
      contentKind = MarkdownKind.REPEATATION_PARENT_TAG,
      contentTag = { _, _ -> "P. " },
    )

    assertThat(node.draw(MarkdownOptions()))
      .isEqualTo(
        "P. Hello, World!\nP. Morning, World!\nP. Bye, World!\n" +
          "P. Hello, World2!\nP. Morning, World2!\nP. Bye, World2!",
      )
  }

  @Test fun drawingGroupInGroup() {
    val childNode = MarkdownNode(
      children = listOf(MarkdownNode(source = { worldSource2 }, kind = MarkdownKind.TEXT)),
      kind = MarkdownKind.GROUP + MarkdownKind.REPEATATION_PARENT_TAG,
      contentKind = MarkdownKind.REPEATATION_PARENT_TAG,
      contentTag = { _, _ -> "- " },
    )
      .apply { index = 1 }

    node = MarkdownNode(
      children = listOf(
        MarkdownNode(source = { worldSource }, kind = MarkdownKind.TEXT),
        childNode,
      ),
      kind = MarkdownKind.GROUP,
      contentKind = MarkdownKind.REPEATATION_PARENT_TAG,
      contentTag = { _, _ -> "> " },
    )

    assertThat(node.draw(MarkdownOptions()))
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
        MarkdownNode(source = { "My ordered list!\nMy ordered list's new line!" })
          .apply { index = actualIndex }
      },
      kind = MarkdownKind.GROUP + MarkdownKind.REPEATATION_PARENT_TAG,
      contentTag = { index, _ -> "${index + 1}. " },
    )
    val childrenNodes = MarkdownNode(
      children = listOf(
        nestedChildrenNodes,
        MarkdownNode(source = { worldSource }, kind = MarkdownKind.TEXT),
        MarkdownNode(source = { worldSource2 }, kind = MarkdownKind.TEXT),
      ),
      kind = MarkdownKind.GROUP,
      contentKind = MarkdownKind.REPEATATION_PARENT_TAG,
      contentTag = { _, _ -> "> " },
    )
    node = MarkdownNode(
      children = listOf(
        MarkdownNode(source = { "Hello!" }),
        childrenNodes,
      ),
      kind = MarkdownKind.GROUP,
      contentTag = { _, _ -> "" },
    )

    assertThat(node.draw(MarkdownOptions()))
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
