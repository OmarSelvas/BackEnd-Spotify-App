pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        // Necesario para la resolución de plugins
        maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
    }
}

rootProject.name = "ApiSpotify"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        // Necesario para la resolución de dependencias
        maven { url = uri("https://maven.pkg.jetbrains.space/public/p/ktor/eap") }
    }
}