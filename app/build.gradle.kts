plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-parcelize")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdk = libs.versions.compileSdk.get().toInt()
    buildFeatures {
        dataBinding = true
    }
    defaultConfig {
        applicationId = "com.wallee.samples.apps.shop"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 5
        versionName = "1.2.0-postfinance-staging"
        vectorDrawables.useSupportLibrary = true
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            isDebuggable = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"

        // Enable Coroutines and Flow APIs
        freeCompilerArgs =
            freeCompilerArgs + "-Xopt-in=kotlinx.coroutines.ExperimentalCoroutinesApi"
        freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlinx.coroutines.FlowPreview"
    }
    buildFeatures {
        compose = true
        dataBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
    packagingOptions {
        // Multiple dependency bring these files in. Exclude them to enable
        // our test APK to build (has no effect on our AARs)
        resources.excludes += "/META-INF/AL2.0"
        resources.excludes += "/META-INF/LGPL2.1"
    }
}

androidComponents {
    onVariants(selector().withBuildType("release")) {
        // Only exclude *.version files in release mode as debug mode requires
        // these files for layout inspector to work.
        it.packaging.resources.excludes.add("META-INF/*.version")
    }
}

dependencies {
    kapt(libs.androidx.room.compiler)
    kapt(libs.hilt.android.compiler)

    api(libs.jjwt.api)
    runtimeOnly(libs.jjwt.impl)
    runtimeOnly(libs.jjwt.orgjson)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.paging.compose)
    implementation(libs.androidx.paging.runtime.ktx)
    implementation(libs.androidx.room.ktx)
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.coil.compose)
    implementation(libs.material)
    implementation(libs.datastore.preferences)
    implementation(libs.gson)
    implementation(libs.okhttp3.logging.interceptor)
    implementation(libs.retrofit2.converter.gson)
    implementation(libs.retrofit2.converter.scalars)
    implementation(libs.retrofit2)
    implementation(platform(libs.kotlin.bom))
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    implementation(libs.androidx.profileinstaller)
    implementation(libs.androidx.tracing.ktx)
    // Wallee Payment SDK
    // implementation(libs.wallee.payment.sdk)

    // POSTFINANCE SDK
    api(project(":postfinance-checkout-sdk-staging"))

    //-----------============= DEPENDENCIES FOR AAR FILE!!!!!!  POSTFINANCE
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.7.10")
    implementation("com.wallee.resources:twint-sdk:1.2.0")
    implementation("com.wallee.resources:react-native:1.0.0")
    implementation("com.wallee.resources:hermes-engine:1.0.0")
    implementation("com.wallee.resources:android-jsc:1.0.0")
    implementation("com.facebook.infer.annotation:infer-annotation:0.18.0")
    implementation("com.facebook.yoga:proguard-annotations:1.19.0")
    implementation("javax.inject:javax.inject:1")
    implementation("androidx.appcompat:appcompat:1.4.1")
    implementation("androidx.appcompat:appcompat-resources:1.4.1")
    implementation("androidx.autofill:autofill:1.1.0")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.0.0")
    implementation("com.facebook.fresco:fresco:2.5.0")
    implementation("com.facebook.fresco:imagepipeline-okhttp3:2.5.0")
    implementation("com.facebook.fresco:ui-common:2.5.0")
    implementation("com.facebook.soloader:soloader:0.10.5")
    implementation("com.google.code.findbugs:jsr305:3.0.2")
    implementation("com.squareup.okhttp3:okhttp:4.9.2")
    implementation("com.squareup.okhttp3:okhttp-urlconnection:4.9.2")
    implementation("com.squareup.okio:okio:2.9.0")
    implementation("com.facebook.fbjni:fbjni-java-only:0.2.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation("org.jetbrains.kotlin:kotlin-android-extensions-runtime:1.7.10")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.wallee.resources:react-native-async-storage_async-storage:1.2.0")
    implementation("com.wallee.resources:react-native-community_datetimepicker:1.2.0")
    implementation("com.wallee.resources:react-native-community_netinfo:1.2.0")
    implementation("com.wallee.resources:react-native-device-info:1.2.0")
    implementation("com.wallee.resources:react-native-file-access:1.2.0")
    implementation("com.wallee.resources:react-native-hash:1.2.0")
    implementation("com.wallee.resources:react-native-svg:1.2.0")
    implementation("com.wallee.resources:react-native-webview:1.2.0")
    //-----------=============



    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.accompanist.themeadapter.material)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.constraintlayout.compose)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.foundation.layout)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.ui.viewbinding)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.runtime.livedata)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.glide)
    debugImplementation(libs.androidx.compose.ui.tooling)
}