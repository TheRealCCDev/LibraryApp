plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.google.devtools.ksp) // <--- Activa el plugin
}




android {
    namespace = "com.example.libraryapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.libraryapp"
        minSdk = 24
        targetSdk = 36
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    // --- NÚCLEO Y COMPOSE ---
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3) // Material 3 para los componentes

    // --- ROOM (Base de Datos) ---
    // Usamos las versiones del catálogo (toml) que configuramos antes
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler) // Procesador de anotaciones

    // --- VIEWMODEL Y CICLO DE VIDA ---
    implementation(libs.androidx.lifecycle.runtime.ktx)
    // Esta es vital para usar viewModel() dentro de LibraryScreen
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    // Para usar collectAsStateWithLifecycle (más eficiente)
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.8.4")

    // --- UTILIDADES ---
    // Coil para cargar imágenes (por si quieres poner portadas de Sanderson)
    implementation("io.coil-kt:coil-compose:2.5.0")
    // DataStore por si guardas preferencias de usuario
    implementation("androidx.datastore:datastore-preferences:1.1.1")

    // WORKMANAGER
    implementation("androidx.work:work-runtime-ktx:2.9.0")

    // --- TESTING ---
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}