android.buildFeatures.buildConfig = true

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    id("com.google.devtools.ksp")
    id("androidx.room")

    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")

    id("org.jetbrains.kotlin.plugin.serialization")
}

android {
    namespace = "com.example.myamover"
    compileSdk = 36



    defaultConfig {
        applicationId = "com.example.myamover"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"


        defaultConfig {
            // ...
            buildConfigField(
                "String",
                "SUPABASE_URL",
                "\"${secrets.propertiesFileName}\""
            )
            buildConfigField(
                "String",
                "SUPABASE_ANON_KEY",
                "\"${secrets.propertiesFileName}\""
            )

        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
}

room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {

    // --- AndroidX Core ---
    implementation(libs.androidx.core.ktx)

    // --- Lifecycle & ViewModel ---
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.savedstate)

    // --- Coroutines ---
    implementation(libs.kotlinx.coroutines.android)

    // --- Compose ---
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.runtime)

    // Material 3
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material3.window.size.class1)
    implementation(libs.androidx.material.icons.extended)

    // Activity Compose
    implementation(libs.androidx.activity.compose)

    // Navigation Compose
    implementation(libs.androidx.navigation.compose)
    // Se usar Hilt para injeção de dependência
    // implementation(libs.androidx.navigation.compose.hilt)
    // implementation(libs.androidx.navigation.compose.hilt.navigation)

    // --- Lottie ---
    implementation(libs.lottie.compose)

    // --- Firebase ---
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)

    // --- Google Play Services ---
    implementation(libs.play.services.cast.framework)
    implementation(libs.androidx.compose.material3.material3)
    implementation(libs.runtime)

    // --- Room ---
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.foundation.layout)
    ksp(libs.androidx.room.compiler)
    annotationProcessor(libs.androidx.room.compiler)

    // --- DataStore ---
    implementation(libs.androidx.datastore.preferences)

    // --- Testes ---
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)

    // --- Debug ---
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //--- Maps ---
    // Maps SDK for Android
    implementation("com.google.android.gms:play-services-maps:19.2.0")
    implementation(libs.maps.compose)
    implementation("com.google.code.gson:gson:2.10.1")

    // Cliente Supabase
    implementation(libs.supabase.kt)

    // (opcionais mas normalmente úteis)
    implementation("io.github.jan-tennert.supabase:postgrest-kt:3.2.5")  // CRUD nas tabelas
    implementation("io.github.jan-tennert.supabase:auth-kt:3.2.5") // Autenticação de utilizadores
    implementation(libs.realtime.kt)    // Subscrições em tempo real

    implementation("io.ktor:ktor-client-android:3.3.1")
}


//google maps
secrets {

    propertiesFileName = "secrets.properties"
    defaultPropertiesFileName = "local.defaults.properties"
}
