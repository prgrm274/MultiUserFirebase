import org.jetbrains.kotlin.gradle.internal.Kapt3GradleSubplugin.Companion.isIncrementalKapt

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed
plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    id("com.google.gms.google-services")
    id("dagger.hilt.android.plugin")
    id("com.google.dagger.hilt.android")
    id("kotlin-kapt")
}

android {
    namespace = "com.programmer270487.loginsignuptypicode"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.programmer270487.loginsignuptypicode"
        minSdk = 33
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        multiDexEnabled = true
    }

    buildTypes {
        debug {
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }

//    packaging {
////        resources {
////            excludes += "/META-INF/{AL2.0,LGPL2.1}"
////        }
//
//        resources.excludes.add("META-INF/*")    //solusi junit5 dependensi di android
//        resources.excludes.add("META-INF/LICENSE")
//        resources.excludes.add("META-INF/LICENSE.txt")
//        resources.excludes.add("META-INF/license.txt")
//        resources.excludes.add("META-INF/NOTICE")
//        resources.excludes.add("META-INF/NOTICE.txt")
//        resources.excludes.add("META-INF/notice.txt")
//        resources.excludes.add("META-INF/ASL2.0")
//        resources.excludes.add("META-INF/*.kotlin_module")
//        resources.excludes.add("META-INF/DEPENDENCIES")
//    }
//    packaging {
//        resources {
//            excludes += "META-INF/INDEX.LIST"
//            excludes += "META-INF/DEPENDENCIES"
//        }
//    }
    packagingOptions {
////        resources.excludes.add("META-INF/INDEX.LIST")
////        resources.excludes.add("META-INF/DEPENDENCIES")
////        exclude("META-INF/INDEX.LIST")
////        exclude("META-INF/DEPENDENCIES")
        exclude("META-INF/gradle/incremental.annotation.processors")
//        exclude("META-INF/LICENSE")
//        exclude("META-INF/LICENSE.txt")
//        exclude("META-INF/license.txt")
//        exclude("META-INF/NOTICE")
//        exclude("META-INF/NOTICE.txt")
//        exclude("META-INF/notice.txt")
//        exclude("META-INF/DEPENDENCIES")
    }
    kapt {
        correctErrorTypes = true
        !isIncrementalKapt()
    }
}

dependencies {

    implementation(libs.core.ktx)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.retrofit)
    implementation(libs.retrofitConverterGson)
    implementation(libs.picasso)
    implementation(libs.hilt)
//    implementation(libs.hiltLifecycle) // don't use since dagger 2.34
//    implementation(libs.hiltAndroidCompiler)
//    implementation(libs.hiltCompiler)
    kapt("com.google.dagger:hilt-android-compiler:2.48")
    kapt("androidx.hilt:hilt-compiler:1.2.0")
    implementation(libs.fragment)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
}