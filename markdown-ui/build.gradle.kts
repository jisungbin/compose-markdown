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
  explicitApi()
}

dependencies {
  implementation(libs.compose.runtime)
  implementation(libs.androidx.annotation)

  api(projects.markdownRuntime)

  testImplementation(kotlin("test-junit5"))
  testImplementation(libs.test.assertk)
  testImplementation(libs.test.kotlin.coroutines)
}
