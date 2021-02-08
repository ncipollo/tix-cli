plugins {
    kotlin("multiplatform") version "1.4.30"
}

group = "me.ncipollo"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    val hostOs = System.getProperty("os.name")
    val isMingwX64 = hostOs.startsWith("Windows")
    val nativeTarget = when {
        hostOs == "Mac OS X" -> macosX64("native")
        hostOs == "Linux" -> linuxX64("native")
        isMingwX64 -> mingwX64("native")
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }

    nativeTarget.apply {
        binaries {
            executable {
                entryPoint = "main"
            }
        }
    }
    linuxX64()
    macosX64()
    mingwX64()
    sourceSets {
        val nativeMain by getting
        val nativeTest by getting
        val linuxX64Main by getting
        val linuxX64Test by getting
        val macosX64Main by getting
        val macosX64Test by getting
        val mingwX64Main by getting
        val mingwX64Test by getting
    }
}
