plugins {
    kotlin("multiplatform") version "1.5.0"
}

group = "org.tix"
version = "0.0.1"

repositories {
    mavenCentral()
}

kotlin {
    listOf(
        linuxX64(),
        macosX64()
    ).forEach {
        it.apply {
            binaries {
                executable {
                    entryPoint = "org.tix.main"
                }
            }
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.tix:core:0.0.1")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.0")
            }
        }
        val commonTest by getting

        val nativeMain by creating { dependsOn(commonMain) }
        val nativeTest by creating { dependsOn(commonTest) }

        val linuxX64Main by getting { dependsOn(nativeMain) }
        val linuxX64Test by getting { dependsOn(nativeTest) }

        val macosX64Main by getting { dependsOn(nativeMain) }
        val macosX64Test by getting { dependsOn(nativeTest) }
    }
}

tasks.register("debugBuild") { cliBuild("Debug") }
tasks.register("releaseBuild") { cliBuild("Release") }

fun Task.cliBuild(buildType: String) {
    val taskSuffix = when (System.getProperty("os.name")) {
        "Mac OS X" -> "MacosX64"
        "Linux" -> "LinuxX64"
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }
    val buildTasks = listOf("compileKotlin${taskSuffix}", "link${buildType}Executable${taskSuffix}")
    buildTasks.forEach {
        dependsOn(tasks.findByName(it))
    }
}