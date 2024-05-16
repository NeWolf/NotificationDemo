pluginManagement {

    repositories {

        google()
        mavenCentral()
        gradlePluginPortal()
        maven ("https://maven.aliyun.com/nexus/content/groups/public/")
        maven ("https://jitpack.io")
    }
}
dependencyResolutionManagement {
//    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven ("https://maven.aliyun.com/nexus/content/groups/public/")
        maven ("https://jitpack.io")
    }
}

rootProject.name = "NotificationDemo"
include(":app")
 