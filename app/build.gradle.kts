plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.proyectofiagrubngil"
    compileSdk = 34 // He ajustado esto a 34 (Android 14) para mayor estabilidad, 36 es preview

    defaultConfig {
        applicationId = "com.example.proyectofiagrubngil"
        minSdk = 24 // Subido a 24 para compatibilidad con librerías modernas
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
    // --- LIBRERÍAS ESTABLES (Manuales) ---
    // Usamos estas versiones específicas para evitar el error de "compileSdk 36"
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.activity:activity:1.8.0")

    // --- LIBRERÍAS DEL PROYECTO (Room, WorkManager, Gson) ---
    implementation("androidx.room:room-runtime:2.6.1")
    annotationProcessor("androidx.room:room-compiler:2.6.1")
    implementation("androidx.work:work-runtime:2.9.0")
    implementation("com.google.code.gson:gson:2.10.1")

    // --- TEST ---
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}