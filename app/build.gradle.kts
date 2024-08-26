plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.rbk.daggerandhilt"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.rbk.daggerandhilt"
        minSdk = 24
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // Retrofit and Gson Converter dependencies
    //initially we were writing libraries as string but standards
    // to add dependency into lib recommended
    // old approach =  implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation(libs.retrofit)
    implementation(libs.retrofit.gsonConverter)
}