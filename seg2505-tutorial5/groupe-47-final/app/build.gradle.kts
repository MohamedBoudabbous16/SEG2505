import com.android.build.api.dsl.Packaging

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)

}

android {

    namespace = "com.example.pcorderapplication"
    compileSdk = 34

    defaultConfig {

        applicationId = "com.example.pcorderapplication"
        minSdk = 30
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
        // Use Gradle's toolchain support for dynamic Java version management
        java {
            toolchain {
                languageVersion = JavaLanguageVersion.of(17)
            }
        }
    }

    buildFeatures {
        viewBinding = true
    }
    packagingOptions {
        resources {
            merges += "META-INF/LICENSE.md"
            merges += "META-INF/LICENSE-notice.md"
        }
    }

}


    dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)

    // Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.5.1"))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-analytics")
    implementation("androidx.room:room-runtime:2.5.1")
    implementation(libs.core)
    testImplementation(libs.androidx.junit)
    testImplementation(libs.androidx.espresso.core)
    annotationProcessor("androidx.room:room-compiler:2.5.1")
    implementation("androidx.cardview:cardview:1.0.0")

    // Unit tests
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.mockito:mockito-core:5.4.0") // Update to latest Mockito
    // No need for mockito-inline with Mockito 5

    // Instrumentation tests
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.test.espresso:espresso-intents:3.5.1")
    androidTestImplementation("androidx.test:runner:1.5.2")
    androidTestImplementation("androidx.test:rules:1.5.0")
    androidTestImplementation("org.mockito:mockito-android:5.4.0") // Use Mockito 5

    androidTestImplementation ("io.mockk:mockk-android:1.13.5") // Use the latest version

    // Remove duplicate Espresso dependency
    // androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1") // Remove if not needed

    // Remove PowerMock dependencies
    // testImplementation("org.powermock:powermock-api-mockito2:2.0.9")
    // testImplementation("org.powermock:powermock-module-junit4:2.0.9")

    // Robolectric (Optional, for unit tests)
     testImplementation("org.robolectric:robolectric:4.10.3")
    testImplementation ("io.mockk:mockk:1.13.5")
}

configurations.all {
    exclude(group = "org.powermock")
}
