## Compose Markdown

```kotlin
StringBuilder().markdown {
  H1("Hello, Compose-Markdown!")
  Quote(
    modifier = Modifier.clickable(link = "https://github.com/jisungbin/compose-markdown"),
    text = buildAnnotatedString {
      append("Build ")
      withStyle(TextStyle(italic = true)) { append("Markdown") }
      append(" with ")
      withStyle(TextStyle(fontWeight = FontWeight.Bold)) { append("Jetpack Compose runtime") }
      append("!")
    },
  )
}
```

WIP
