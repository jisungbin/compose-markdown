package land.sungbin.markdown.ui.image

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.test.Test
import land.sungbin.markdown.ui.render
import land.sungbin.markdown.ui.unit.FixedSize
import okio.buffer

class ImageTest {
  @Test fun urlOnly() {
    val image = ImageNode("https://example.com/image.jpg")
    assertThat(image.render().buffer().readUtf8())
      .isEqualTo("<img src=\"https://example.com/image.jpg\" />")
  }

  @Test fun urlAndSize() {
    val image = ImageNode("https://example.com/image.jpg", size = FixedSize(100, 200))
    assertThat(image.render().buffer().readUtf8()).isEqualTo(
      "<img " +
        "src=\"https://example.com/image.jpg\" " +
        "width=\"100\" height=\"200\" " +
        "/>",
    )
  }

  @Test fun urlAndAlt() {
    val image = ImageNode("https://example.com/image.jpg", alt = "An image")
    assertThat(image.render().buffer().readUtf8()).isEqualTo(
      "<img " +
        "src=\"https://example.com/image.jpg\" " +
        "alt=\"An image\" " +
        "/>",
    )
  }

  @Test fun urlSizeAndAlt() {
    val image = ImageNode(
      "https://example.com/image.jpg",
      size = FixedSize(100, 200),
      alt = "An image",
    )
    assertThat(image.render().buffer().readUtf8()).isEqualTo(
      "<img " +
        "src=\"https://example.com/image.jpg\" " +
        "width=\"100\" height=\"200\" " +
        "alt=\"An image\" " +
        "/>",
    )
  }
}