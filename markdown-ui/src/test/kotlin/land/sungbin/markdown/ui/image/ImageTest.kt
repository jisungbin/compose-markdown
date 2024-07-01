/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/compose-markdown/blob/main/LICENSE
 */

package land.sungbin.markdown.ui.image

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.test.Test
import land.sungbin.markdown.ui.unit.FixedSize

class ImageTest {
  @Test fun urlOnly() {
    val image = buildImageText("https://example.com/image.jpg")
    assertThat(image).isEqualTo("<img src=\"https://example.com/image.jpg\" />")
  }

  @Test fun urlAndSize() {
    val image = buildImageText("https://example.com/image.jpg", size = FixedSize(100, 200))
    assertThat(image).isEqualTo(
      "<img " +
        "src=\"https://example.com/image.jpg\" " +
        "width=\"100\" height=\"200\" " +
        "/>",
    )
  }

  @Test fun urlAndAlt() {
    val image = buildImageText("https://example.com/image.jpg", alt = "An image")
    assertThat(image).isEqualTo(
      "<img " +
        "src=\"https://example.com/image.jpg\" " +
        "alt=\"An image\" " +
        "/>",
    )
  }

  @Test fun urlSizeAndAlt() {
    val image = buildImageText(
      "https://example.com/image.jpg",
      size = FixedSize(100, 200),
      alt = "An image",
    )
    assertThat(image).isEqualTo(
      "<img " +
        "src=\"https://example.com/image.jpg\" " +
        "width=\"100\" height=\"200\" " +
        "alt=\"An image\" " +
        "/>",
    )
  }
}
