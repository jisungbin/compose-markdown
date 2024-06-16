package land.sungbin.markdown.ui.text

import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import land.sungbin.markdown.runtime.MarkdownComposable
import land.sungbin.markdown.runtime.node.Text
import okio.buffer

@MarkdownComposable
@Composable
@NonRestartableComposable
public fun AnnotatedText(annotated: AnnotatedString) {
  Text(text = annotated.text().buffer().readUtf8())
}
