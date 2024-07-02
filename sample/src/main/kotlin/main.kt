/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/compose-markdown/blob/main/LICENSE
 */

import kotlinx.coroutines.runBlocking
import land.sungbin.markdown.runtime.markdown
import land.sungbin.markdown.ui.list.List
import land.sungbin.markdown.ui.modifier.Modifier
import land.sungbin.markdown.ui.modifier.clickable
import land.sungbin.markdown.ui.modifier.footnote
import land.sungbin.markdown.ui.quote.Quote
import land.sungbin.markdown.ui.text.Code
import land.sungbin.markdown.ui.text.H2
import land.sungbin.markdown.ui.text.H3
import land.sungbin.markdown.ui.text.Text
import land.sungbin.markdown.ui.text.TextStyle
import land.sungbin.markdown.ui.text.buildAnnotatedString

fun main() = runBlocking {
  val readme = markdown {
    H2("Compose Markdown")
    Text("")
    Text(
      buildAnnotatedString {
        append("Build markdown with Jetpack Compose ")
        withStyle(TextStyle(italics = true)) { "runtime" }
        append(".")
      },
      modifier = Modifier
        .clickable("https://developer.android.com/jetpack/androidx/releases/compose-runtime") { text ->
          text.indexOf("runtime").let { it..it + "runtime".length }
        }
        .footnote("UI", position = { it.length - 1 }) {
          Text(buildAnnotatedString { withStyle(TextStyle(bold = true)) { "Not Compose UI!" } })
        },
    )
    Text("")
    Quote {
      Text(
        "This README is made with Composable! Check out the sample.",
        modifier = Modifier.clickable("/sample/src/main/kotlin/main.kt") { text ->
          text.indexOf("sample").let { it..<it + "sample".length }
        },
      )
    }
    Text("")
    H3("Introduction")
    Text("")
    Text("Jetpack Compose is often known as a UI toolkit, but it is actually a library providing excellent ")
    Text("node traversal implementation. This repository creates our own Compose UI that produces Markdown ")
    Text("strings using only Compose's Runtime features.")
    Text("")
    Text("This development began with the purpose of learning Compose Runtime and has been made public to ")
    Text("spread the value of the Compose Runtime.")
    Text("")
    Text("You can generate markdown programmatically using the power of `Kotlin + Composable`.")
    Text("")
    Code("kotlin") {
      Text("List(ordered = true) {")
      Text("  repeat(3) {")
      Text("    Text(\"My item!\")")
      Text("  }")
      Text("}")
    }
    Text("")
    List(ordered = true) {
      repeat(3) {
        Text("My item!")
      }
    }
    Text("")
    H3("Download")
    Text("")
    Text("Will be published to MavenCentral soon.")
  }
  println(readme)
}
