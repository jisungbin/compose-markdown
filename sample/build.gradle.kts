/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/compose-markdown/blob/main/LICENSE
 */

plugins {
  kotlin("jvm")
  alias(libs.plugins.kotlin.compose)
}

kotlin {
  sourceSets.all {
    languageSettings.enableLanguageFeature("ExplicitBackingFields")
  }
}

dependencies {
  implementation(projects.markdownUi)
}
