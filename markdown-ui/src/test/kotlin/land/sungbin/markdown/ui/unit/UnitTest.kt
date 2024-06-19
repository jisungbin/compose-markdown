package land.sungbin.markdown.ui.unit

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.test.Test

class UnitTest {
  @Test fun fixed() {
    val size = FixedSize(100, 200)
    assertThat(size.width).isEqualTo("100")
    assertThat(size.height).isEqualTo("200")
  }

  @Test fun percentage() {
    val size = Percentage(55.5f, 70.2f)
    assertThat(size.width).isEqualTo("56%")
    assertThat(size.height).isEqualTo("70%")
  }
}