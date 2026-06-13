plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    namespace = "com.example.bloodpressuretracker"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.bloodpressuretracker"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    // ✅ ДОБАВЛЕН БЛОК buildTypes
    buildTypes {
        release {
            // Отключаем сжатие кода и обфускацию для упрощения (для учебного проекта)
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    constraints {
        implementation("androidx.activity:activity:1.8.2")
        implementation("androidx.core:core:1.12.0")
        implementation("androidx.lifecycle:lifecycle-runtime:2.7.0")
    }
    implementation("androidx.core:core-ktx:1.12.0")          // было 1.18.0
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // Room
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")

    // Lifecycle (ViewModel + LiveData)
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.7.0")

    // RecyclerView
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    // Chart (MPAndroidChart)
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

    // WorkManager
    implementation("androidx.work:work-runtime-ktx:2.8.1")   // понижена

    // Явно указываем стабильную версию activity (чтобы избежать 1.13.0)
    implementation("androidx.activity:activity-ktx:1.8.2")
    implementation("androidx.fragment:fragment-ktx:1.6.2")

    // Для стабильности core и других
    implementation("androidx.core:core:1.12.0")
}