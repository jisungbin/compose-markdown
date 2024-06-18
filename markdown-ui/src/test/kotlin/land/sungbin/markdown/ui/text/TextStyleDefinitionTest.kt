package land.sungbin.markdown.ui.text

import assertk.assertThat
import assertk.assertions.isEqualTo
import kotlin.test.Test
import land.sungbin.markdown.ui.transform

class TextStyleDefinitionTest {
  @Test fun bold() {
    val styled = TextStyleDefinition.Bold.transform("안녕하세요 hello~")
    assertThat(styled.buffer.readUtf8()).isEqualTo("**안녕하세요 hello~**")
  }

  @Test fun italic() {
    val styled = TextStyleDefinition.Italic.transform("안녕하세요 hello~")
    assertThat(styled.buffer.readUtf8()).isEqualTo("_안녕하세요 hello~_")
  }

  @Test fun strikethrough() {
    val styled = TextStyleDefinition.Strikethrough.transform("안녕하세요 hello~")
    assertThat(styled.buffer.readUtf8()).isEqualTo("~~안녕하세요 hello~~~")
  }

  @Test fun underline() {
    val styled = TextStyleDefinition.Unerline.transform("안녕하세요 hello~")
    assertThat(styled.buffer.readUtf8()).isEqualTo("<ins>안녕하세요 hello~</ins>")
  }

  @Test fun monospace() {
    val styled = TextStyleDefinition.Monospace.transform("안녕하세요 hello~")
    assertThat(styled.buffer.readUtf8()).isEqualTo("`안녕하세요 hello~`")
  }
}
