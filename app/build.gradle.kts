import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackConfig

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.fromTarget(libs.versions.jvmTarget.get()))
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }

    // WASM 타겟 다시 추가 - Kodein-DI 사용
    @OptIn(org.jetbrains.kotlin.gradle.ExperimentalWasmDsl::class)
    wasmJs {
        outputModuleName.set("composeApp")

        // 컴파일러 옵션 설정 - 문자 인코딩 지원
        compilerOptions {
            freeCompilerArgs.addAll(
                "-Xwasm-use-new-exception-proposal",
                "-opt-in=kotlin.ExperimentalUnsignedTypes"
            )
        }

        browser {
            val rootDirPath = project.rootDir.path
            val projectDirPath = project.projectDir.path
            commonWebpackConfig {
                outputFileName = "composeApp.js"
                devServer = (devServer ?: KotlinWebpackConfig.DevServer()).apply {
                    static = (static ?: mutableListOf()).apply {
                        // Serve sources to debug inside browser
                        add(rootDirPath)
                        add(projectDirPath)
                    }
                    open = true          // 자동으로 브라우저 열기
                    port = 8080          // 기본 포트 (변경 가능)
                }
                // 한글 문자 인코딩 처리를 위한 설정
                experiments.add("topLevelAwait")
            }
        }
        binaries.executable()
    }

    // JVM Toolchain 설정
    jvmToolchain(libs.versions.jvmToolchain.get().toInt())

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)

            // DateTime support for KMP
            implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.0")

            // kotlin-inject for DI - 올바른 버전 사용
            implementation(libs.kotlinInjectRuntime)
            implementation(project(":modules:presentation"))
            implementation(project(":modules:domain"))
            implementation(project(":modules:data"))
            
            // Koin DI (수동 방식만 사용)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
        }

        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
        }

        iosMain.dependencies {
            // iOS는 commonMain의 kotlin-inject 사용
        }

        wasmJsMain.dependencies {
            // WASM은 commonMain의 kotlin-inject 사용
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.core)
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.1")
        }
    }
}

android {
    namespace = "org.example.project"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "org.example.project"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    testOptions {
        unitTests {
            isIncludeAndroidResources = true
            all {
                it.useJUnitPlatform()
            }
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.toVersion(libs.versions.javaVersion.get())
        targetCompatibility = JavaVersion.toVersion(libs.versions.javaVersion.get())
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}
