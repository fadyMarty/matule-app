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
    }
}

rootProject.name = "Matule"
include(":app")

fun initSubmodules() {
    val submodulePaths = listOf("matule_network", "matule_ui_kit")

    val needsInit = submodulePaths.any { path ->
        file(path).listFiles().isEmpty()
    }

    if (needsInit) {
        ProcessBuilder()
            .command("git", "submodule", "update", "--init", "--recursive")
            .directory(rootDir)
            .redirectErrorStream(true)
            .start()
            .waitFor()
    }
}

initSubmodules()

include(":network")
project(":network").projectDir = File("matule_network/network")

include(":ui_kit")
project(":ui_kit").projectDir = File("matule_ui_kit/ui_kit")