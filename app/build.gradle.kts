plugins {
    // Apply the shared build logic from a convention plugin.
    // The shared code is located in `buildSrc/src/main/kotlin/kotlin-jvm.gradle.kts`.
//    id("buildsrc.convention.kotlin-jvm")
    kotlin("multiplatform")
    alias(libs.plugins.zipline)
}

kotlin {
    jvm {
    }
    js {
        nodejs()
        binaries.executable()
    }
    sourceSets {
        commonMain.dependencies {
            implementation(libs.zipline)
        }
        jvmMain.dependencies {
//            implementation(project.dependencies.platform("com.squareup.okhttp3:okhttp-bom:5.3.0"))
            implementation("com.squareup.okhttp3:okhttp:5.3.0")

            implementation(libs.zipline.loader)
            implementation(libs.kotlinxCoroutines)
            implementation(libs.kotlinxSerialization)
        }
        jvmTest.dependencies {
            implementation(kotlin("test"))
        }
    }
}

zipline {
}
