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
        maven("https://jitpack.io")
        maven("https://s01.oss.sonatype.org/content/groups/public")
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io")
        maven("https://s01.oss.sonatype.org/content/groups/public")
    }

}

rootProject.name = "Wanandroid"
include(":app-compose")
include(":app-mvp-java")
include(":app-mvvm-kotlin")
include(":common")
include(":model")
include(":common-res")
