package land.sungbin.markdown.ui.image

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.test.Test
import land.sungbin.markdown.ui.unit.FixedSize

class ImageTest {
  @Test fun urlOnly() {
    val image = ImageText("https://example.com/image.jpg")
    assertThat(image.source { readUtf8() })
      .isEqualTo("<img src=\"https://example.com/image.jpg\" />")
  }

  @Test fun urlAndSize() {
    val image = ImageText("https://example.com/image.jpg", size = FixedSize(100, 200))
    assertThat(image.source { readUtf8() }).isEqualTo(
      "<img " +
        "src=\"https://example.com/image.jpg\" " +
        "width=\"100\" height=\"200\" " +
        "/>",
    )
  }

  @Test fun urlAndAlt() {
    val image = ImageText("https://example.com/image.jpg", alt = "An image")
    assertThat(image.source { readUtf8() }).isEqualTo(
      "<img " +
        "src=\"https://example.com/image.jpg\" " +
        "alt=\"An image\" " +
        "/>",
    )
  }

  @Test fun urlSizeAndAlt() {
    val image = ImageText(
      "https://example.com/image.jpg",
      size = FixedSize(100, 200),
      alt = "An image",
    )
    assertThat(image.source { readUtf8() }).isEqualTo(
      "<img " +
        "src=\"https://example.com/image.jpg\" " +
        "width=\"100\" height=\"200\" " +
        "alt=\"An image\" " +
        "/>",
    )
  }
}