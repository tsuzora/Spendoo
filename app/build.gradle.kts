plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.gms.google.services)


//    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.spendoov2"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.spendoov2"
        minSdk = 33
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.ui)
    implementation(libs.androidx.compose.ui.ui)
    implementation(libs.ui.graphics)
    implementation(libs.androidx.runtime)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    //implementation(platform("com.google.firebase:firebase-bom:34.4.0"))
    implementation("com.google.firebase:firebase-analytics")

    // Firebase Bill of Materials (BOM) - Mengatur versi
    implementation(platform("com.google.firebase:firebase-bom:33.1.1"))

    // 1. Firebase Authentication
    implementation("com.google.firebase:firebase-auth")

    // 2. Cloud Firestore (Database)
    implementation("com.google.firebase:firebase-firestore")

    implementation(platform(libs.firebase.bom))

    // For ViewModel in Compose
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.3")

    // For observing StateFlows correctly
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.3")

    // For launching coroutines in the ViewModel
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.3")

    // For cleaner Firebase async calls (optional, but recommended)
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.8.1")

    // For JSON conversion
    implementation("com.google.code.gson:gson:2.10.1")

    // Your other dependencies
    implementation(libs.androidx.core.ktx)

    implementation("com.google.android.gms:play-services-auth:21.2.0")
}