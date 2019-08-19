import org.jetbrains.kotlin.gradle.internal.AndroidExtensionsExtension

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
}

//Parcelable annotation
androidExtensions {
    configure(delegateClosureOf<AndroidExtensionsExtension> {
        isExperimental = true
    })
}

android {
    compileSdkVersion(28)
    defaultConfig {
        applicationId = "com.example.baserepository"
        minSdkVersion(15)
        targetSdkVersion(28)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }

        getByName("debug") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }

    //Compile with java 8
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        setTargetCompatibility(JavaVersion.VERSION_1_8)
    }

    //Enable data binding
    dataBinding {
        isEnabled = true
    }

}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation(kotlin("stdlib-jdk7", "1.3.21"))
    implementation("androidx.appcompat:appcompat:1.0.2")
    implementation("androidx.core:core-ktx:1.2.0-alpha02")
    implementation("androidx.constraintlayout:constraintlayout:1.1.3")

    //Test
    testImplementation("junit:junit:4.12")
    androidTestImplementation("androidx.test:runner:1.2.0")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.2.0")

    //Navigation
    implementation("android.arch.navigation:navigation-fragment:1.0.0")
    implementation("android.arch.navigation:navigation-ui:1.0.0")
}
