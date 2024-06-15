/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/compose-markdown/blob/main/LICENSE
 */

import org.jetbrains.dokka.DokkaConfiguration.Visibility

plugins {
  kotlin("jvm")
  alias(libs.plugins.kotlin.dokka)
}

tasks.dokkaHtml {
  moduleName.set("Compose-Markdown Runtime API")
  moduleVersion.set(project.property("VERSION_NAME") as String)
  outputDirectory.set(rootDir.resolve("documentation/site/runtime/api"))

  dokkaSourceSets.configureEach {
    jdkVersion.set(17)
    documentedVisibilities.set(setOf(Visibility.PUBLIC, Visibility.PROTECTED))
  }

  pluginsMapConfiguration.set(
    mapOf(
      "org.jetbrains.dokka.base.DokkaBase" to
        """{ "footerMessage": "compose-markdown ⓒ 2024 Ji Sungbin" }""",
    )
  )
}

kotlin {
  explicitApi()
}

dependencies {
  implementation(libs.compose.runtime)
  implementation(libs.jetbrains.annotation)

  testImplementation(kotlin("test-junit5"))
  testImplementation(libs.test.assertk)
  testImplementation(libs.test.kotlin.coroutines)

  // noinspection UseTomlInstead
  testImplementation("androidx.compose.runtime:runtime-test-utils:1.8.0-SNAPSHOT") {
    isTransitive = false

    // Why snapshot?
    because("https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:compose/runtime/runtime-test-utils/build.gradle;l=68;drc=aa3aa01c08fc9d9e7c13260b4f2fe89dfa2a58f1")
  }

  kotlinCompilerPluginClasspathTest(libs.kotlin.compose.compiler.embeddable)
}
