plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
}

android {
    namespace = "com.fcascan.challengepds"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.fcascan.challengepds"
        minSdk = 26
        targetSdk = 33
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    viewBinding {
        enable = true
    }
}

dependencies {
    //Standard Libraries:
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //Kapt:
//    kapt("groupId:artifactId:version")

    //Viewmodel and livedata
    //https://developer.android.com/jetpack/androidx/releases/lifecycle
    val lifecycleVersion = "2.6.1"
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion")

    //Annotation processor
//    kapt("androidx.lifecycle:lifecycle-compiler:2.6.1")

    //Retrofit https://square.github.io/retrofit/ - latest vesion https://github.com/square/retrofit.
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")   //Gson -> json data to kotlin format

    //Room:
    //https://developer.android.com/jetpack/androidx/releases/room?hl=es-419
    val roomVersion = "2.5.0"
    implementation("androidx.room:room-runtime:$roomVersion")
    implementation("androidx.room:room-ktx:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")
}
