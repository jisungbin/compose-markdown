/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/compose-markdown/blob/main/LICENSE
 */

import com.diffplug.gradle.spotless.SpotlessExtension
import org.jetbrains.dokka.DokkaConfiguration.Visibility
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
  alias(libs.plugins.spotless) apply false
  alias(libs.plugins.kotlin.dokka) apply false
  alias(libs.plugins.gradle.publish.maven) apply false
  idea
}

idea {
  module {
    excludeDirs = excludeDirs + allprojects.map { it.file(".kotlin") }
  }
}

buildscript {
  dependencies {
    classpath(libs.kotlin.gradle)
  }
}

subprojects {
  if (project.hasProperty("GROUP") && project.hasProperty("VERSION_NAME")) {
    group = project.property("GROUP") as String
    version = project.property("VERSION_NAME") as String
  }

  apply {
    plugin(rootProject.libs.plugins.spotless.get().pluginId)
    if (pluginManager.hasPlugin(rootProject.libs.plugins.gradle.publish.maven.get().pluginId)) {
      plugin(rootProject.libs.plugins.kotlin.dokka.get().pluginId)
    }
  }

  extensions.configure<SpotlessExtension> {
    kotlin {
      target("**/*.kt")
      targetExclude("**/build/**/*.kt")
      ktlint(rootProject.libs.versions.ktlint.get()).editorConfigOverride(
        mapOf(
          "indent_size" to "2",
          "ktlint_standard_filename" to "disabled",
          "ktlint_standard_package-name" to "disabled",
          "ktlint_standard_function-naming" to "disabled",
          "ktlint_standard_property-naming" to "disabled",
          "ktlint_standard_backing-property-naming" to "disabled",
          "ktlint_standard_class-signature" to "disabled",
          "ktlint_standard_import-ordering" to "disabled",
          "ktlint_standard_max-line-length" to "disabled",
          "ktlint_standard_annotation" to "disabled",
          "ktlint_standard_multiline-if-else" to "disabled",
          "ktlint_standard_value-argument-comment" to "disabled",
          "ktlint_standard_value-parameter-comment" to "disabled",
          "ktlint_standard_comment-wrapping" to "disabled",
        )
      )
      licenseHeaderFile(rootProject.file("spotless/copyright.kt"))
    }
    format("kts") {
      target("**/*.kts")
      targetExclude("**/build/**/*.kts")
      // Look for the first line that doesn't have a block comment (assumed to be the license)
      licenseHeaderFile(rootProject.file("spotless/copyright.kts"), "(^(?![\\/ ]\\*).*$)")
    }
    format("xml") {
      target("**/*.xml")
      targetExclude("**/build/**/*.xml")
      // Look for the first XML tag that isn't a comment (<!--) or the xml declaration (<?xml)
      licenseHeaderFile(rootProject.file("spotless/copyright.xml"), "(<[^!?])")
    }
  }

  tasks.withType<KotlinCompile> {
    compilerOptions {
      jvmTarget = JvmTarget.JVM_17
      optIn.addAll("-opt-in=kotlin.OptIn", "-opt-in=kotlin.RequiresOptIn")

      // https://github.com/ZacSweers/redacted-compiler-plugin/blob/c866a8ae7b2ab039fee9709c990a5478ac0dc0c7/redacted-compiler-plugin-gradle/build.gradle.kts#L91-L94
      if (project.hasProperty("POM_ARTIFACT_ID")) {
        moduleName = project.property("POM_ARTIFACT_ID") as String
      }
    }
  }

  tasks.withType<Test> {
    useJUnitPlatform()
    outputs.upToDateWhen { false }
  }

  afterEvaluate {
    if (pluginManager.hasPlugin(rootProject.libs.plugins.kotlin.dokka.get().pluginId)) {
      val artifact = name.substringAfter('-')
      tasks.withType<DokkaTask>().configureEach {
        moduleName.set("Compose-Markdown ${artifact.replaceFirstChar(Char::uppercase)} API")
        moduleVersion.set(project.property("VERSION_NAME") as String)
        outputDirectory.set(rootDir.resolve("documentation/site/$artifact/api"))

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
    }
  }
}

tasks.register<Delete>("cleanAll") {
  allprojects.map { project -> project.layout.buildDirectory }.forEach(::delete)
}
