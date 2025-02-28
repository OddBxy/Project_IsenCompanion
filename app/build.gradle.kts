import org.jetbrains.kotlin.konan.properties.Properties
import java.io.FileInputStream

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp")

}

android {
    namespace = "fr.isen.LANIER.isensmartcompanion"
    compileSdk = 35

    defaultConfig {
        applicationId = "fr.isen.LANIER.isensmartcompanion"
        minSdk = 25
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

/*        val localProperties = Properties().apply {
            val localPropertiesFile = rootProject.file("local.properties")
            localPropertiesFile.inputStream().use { it }
        }*/

        val localProperties = Properties()
        localProperties.load( FileInputStream(project.rootProject.file("local.properties")) )

        manifestPlaceholders["API_KEY"] = localProperties["API_KEY"] ?: "NONE"

        //println("key : ${localProperties.getProperty("API_KEY")}") //ok

        buildConfigField("String", "API_KEY", "\"${localProperties.getProperty("API_KEY")}\"")
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
        buildConfig = true
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
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    val nav_version = "2.8.7"
    implementation("androidx.navigation:navigation-compose:$nav_version")

    val req_version = "2.11.0"
    implementation("com.squareup.retrofit2:retrofit:$req_version")
    implementation("com.squareup.retrofit2:converter-gson:$req_version" )


    // Import the BoM for the Firebase platform
    implementation(platform("com.google.firebase:firebase-bom:33.9.0"))
    // Add the dependency for the Vertex AI in Firebase library
    // When using the BoM, you don't specify versions in Firebase library dependencies
    implementation("com.google.firebase:firebase-vertexai")
    implementation("com.google.ai.client.generativeai:generativeai:0.9.0")

    val room_version = "2.6.1"
    implementation("androidx.room:room-runtime:$room_version")
    ksp("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-ktx:$room_version")

    val core_version = "1.15.0"
    implementation("androidx.core:core-ktx:$core_version")
    implementation("androidx.work:work-runtime-ktx:2.8.1")
}

