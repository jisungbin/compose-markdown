## Compose Markdown

Build markdown with Jetpack Compose [_runtime_](https://developer.android.com/jetpack/androidx/releases/compose-runtime).[^UI]

> This README is made with Composable! Check out the [sample](/sample/src/main/kotlin/main.kt).

### Introduction

Jetpack Compose is often known as a UI toolkit, but it is actually a library providing excellent 
node traversal implementation. This repository creates our own Compose UI that produces Markdown 
strings using only Compose's Runtime features.

This development began with the purpose of learning Compose Runtime and has been made public to 
spread the value of the Compose Runtime.

You can generate markdown programmatically using the power of `Kotlin + Composable`.

```kotlin
List(ordered = true) {
  repeat(3) {
    Text("My item!")
  }
}
```

1. My item!
2. My item!
3. My item!

### Download

Will be published to MavenCentral soon.

[^UI]: **Not Compose UI!**
