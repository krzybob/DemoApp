plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("dagger.hilt.android.plugin")
    id("com.google.devtools.ksp")
    id("kotlin-parcelize")
}

android {
    namespace = "pl.bobinski.demo"
    compileSdk = Sdk.compile

    defaultConfig {
        applicationId = "pl.bobinski.demo"
        minSdk = Sdk.minimum
        targetSdk = Sdk.target
        versionCode = 1
        versionName = "1.0"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isDefault = true
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = Versions.java
        targetCompatibility = Versions.java
    }
    kotlinOptions {
        jvmTarget = Versions.java.majorVersion
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Versions.compose_compiler
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:${Versions.core_ktx}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutines}")
    implementation("com.google.dagger:hilt-android:${Versions.hilt}")
    ksp("com.google.dagger:hilt-compiler:${Versions.hilt}")
    implementation("androidx.compose.ui:ui:${Versions.compose}")
    implementation("androidx.compose.ui:ui-tooling:${Versions.compose}")
    implementation("androidx.compose.foundation:foundation:${Versions.compose}")
    implementation("androidx.compose.material3:material3:${Versions.compose_material3}")
    implementation("androidx.activity:activity-compose:${Versions.compose_activity}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:${Versions.lifecycle}")
    implementation("androidx.navigation:navigation-compose:${Versions.compose_navigation}")
    implementation("androidx.hilt:hilt-navigation-compose:${Versions.hilt_navigation}")
    implementation("androidx.paging:paging-compose:${Versions.paging}")
    implementation("io.github.raamcosta.compose-destinations:core:${Versions.destinations}")
    ksp("io.github.raamcosta.compose-destinations:ksp:${Versions.destinations}")
    implementation("com.squareup.retrofit2:retrofit:${Versions.retrofit}")
    implementation("com.squareup.retrofit2:converter-moshi:${Versions.retrofit}")
    implementation("com.squareup.okhttp3:logging-interceptor:${Versions.okhttp}")
    implementation("com.squareup.moshi:moshi:${Versions.moshi}")
    implementation("com.squareup.moshi:moshi-adapters:${Versions.moshi}")
    ksp("com.squareup.moshi:moshi-kotlin-codegen:${Versions.moshi}")
    implementation("io.coil-kt:coil-compose:${Versions.coil}")
    implementation("com.jakewharton.timber:timber:${Versions.timber}")
}