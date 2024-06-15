/*
 * Developed by Ji Sungbin 2024.
 *
 * Licensed under the MIT.
 * Please see full license: https://github.com/jisungbin/compose-markdown/blob/main/LICENSE
 */

@file:Suppress("UnstableApiUsage")

rootProject.name = "compose-markdown"

pluginManagement {
  repositories {
    google {
      content {
        includeGroupByRegex("com\\.android.*")
        includeGroupByRegex("com\\.google.*")
        includeGroupByRegex("androidx.*")
      }
    }
    mavenCentral()
    gradlePluginPortal()
  }
}
dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories {
    google()
    mavenCentral()
    maven("https://androidx.dev/snapshots/builds/11964836/artifacts/repository") {
      mavenContent {
        includeModuleByRegex("androidx\\.compose\\.runtime", "runtime-test-utils.*")
        snapshotsOnly()
      }
    }
  }
}

include(":compose-markdown")
 