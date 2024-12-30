import java.io.FileInputStream
import java.util.Properties


plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.compose.compiler)
}

val localProperties = Properties()
file("local.properties").takeIf { it.exists() }?.let {
    localProperties.load(FileInputStream(it))
    localProperties.forEach { (key, value) ->
        project.extra[key.toString()] = value
    }
}

android {
    namespace = "com.edgar.googletranslatejetpackcompose"
    compileSdk = 35


    defaultConfig {
        applicationId = "com.edgar.googletranslatejetpackcompose"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildFeatures {
        compose = true
        buildConfig = true  // Enable BuildConfig feature
    }

    buildTypes {
        debug {



            buildConfigField("String", "API_KEY", "\"api_key\"")
            buildConfigField(
                "String",
                "TRANSLATION_API_ENDPOINT",
                "\"https://translation.googleapis.com/language/translate/v2\""
            )
            buildConfigField("String", "BASE_URL", "\"https://translation.googleapis.com/\"")
            buildConfigField(
                "String",
                "LANGUAGES_API_ENDPOINT",
                "\"https://translation.googleapis.com/language/translate/v2/languages\""
            )
        }

        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            // Load API_KEY from local.properties


            buildConfigField("String", "API_KEY", "\"api_key\"")
            buildConfigField(
                "String",
                "TRANSLATION_API_ENDPOINT",
                "\"https://translation.googleapis.com/language/translate/v2\""
            )
            buildConfigField("String", "BASE_URL", "\"https://translation.googleapis.com/\"")
            buildConfigField(
                "String",
                "LANGUAGES_API_ENDPOINT",
                "\"https://translation.googleapis.com/language/translate/v2/languages\""
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

    composeCompiler {
        enableStrongSkippingMode = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation (libs.google.cloud.translate)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.koin.compose)
    implementation(libs.bundles.ktor)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.compose.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
