plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.androiddevelopment"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.androiddevelopment"
        minSdk = 26
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
}
dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.annotation)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation("androidx.recyclerview:recyclerview:1.2.1")

    // For unit tests
    testImplementation(libs.junit) // Ensure this points to a valid JUnit version
    // For Android instrumentation tests
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
