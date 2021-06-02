plugins {
    kotlin("multiplatform") version "1.4.31"
}

group = "org.tix"
version = "0.0.1"

repositories {
    mavenCentral()
}

kotlin {
    val hostOs = System.getProperty("os.name")
    val nativeTarget = when {
        hostOs == "Mac OS X" -> macosX64("native")
        hostOs == "Linux" -> linuxX64("native")
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }

    nativeTarget.apply {
        binaries {
            executable {
                entryPoint = "org.tix.main"
            }
        }
    }
    linuxX64()
    macosX64()
    mingwX64()
    sourceSets {
        val nativeMain by getting {
            dependencies {
                implementation("org.tix:core")
            }
        }
        val nativeTest by getting
        val linuxX64Main by getting
        val linuxX64Test by getting
        val macosX64Main by getting
        val macosX64Test by getting
    }
}
