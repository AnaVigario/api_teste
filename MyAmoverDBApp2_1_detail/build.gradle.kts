plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false

    id("com.google.devtools.ksp") version "2.2.0-2.0.2" apply false
    id("androidx.room") version "2.8.3" apply false

    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin") version "2.0.1" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.24" apply false

}

buildscript {
    dependencies {
        classpath(libs.secrets.gradle.plugin)
    }
}




