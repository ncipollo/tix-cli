import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING
import org.apache.tools.ant.taskdefs.condition.Os
import org.jetbrains.kotlin.builtins.StandardNames.FqNames.target
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTargetWithHostTests
import org.jetbrains.kotlin.gradle.plugin.mpp.NativeBuildType

repositories {
    mavenCentral()
    maven {
        url = uri("https://s01.oss.sonatype.org/content/repositories/snapshots/")
        mavenContent {
            snapshotsOnly()
        }
    }
}

plugins {
    kotlin("multiplatform") version "1.8.21"
    id("com.codingfeline.buildkonfig") version "0.13.3"
}

group = "org.tix"
version = "0.0.1"

kotlin {
    val platforms = listOf(linuxX64(), macosX64(), macosArm64())
    platforms.forEach {
        it.apply {
            binaries {
                executable {
                    baseName = "tix"
                    entryPoint = "org.tix.main"
                }
            }
        }
    }

    registerBuildTasks(platforms)

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("io.github.ncipollo.tix:core:1.0.0-SNAPSHOT") {
                    isChanging = true
                }

                implementation("com.github.ajalt.clikt:clikt:3.5.2")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
            }
        }
        val commonTest by getting

        // Manually creating native targets because the standard native targets don't seem to work correctly with
        // dependencies which are also Kotlin MPP (in this case tix-core).
        val nativeMain by creating { dependsOn(commonMain) }
        val nativeTest by creating { dependsOn(commonTest) }

        val linuxX64Main by getting { dependsOn(nativeMain) }
        val linuxX64Test by getting { dependsOn(nativeTest) }

        val macosArm64Main by getting { dependsOn(nativeMain) }
        val macosArm64Test by getting { dependsOn(nativeTest) }
        val macosX64Main by getting { dependsOn(nativeMain) }
        val macosX64Test by getting { dependsOn(nativeTest) }
    }
}

buildkonfig {
    packageName = "org.tix.cli.config"
    exposeObjectWithName = "TixCLIConfig"
    defaultConfigs {
        buildConfigField(STRING, "version", version.toString())
    }
}

fun registerBuildTasks(platforms: List<KotlinNativeTargetWithHostTests>) {
    // These tasks were create because the standard build task relies on "nativeBuild" which no longer exists.
    tasks.register("debugBuild") { cliBuild("Debug") }
    tasks.register("releaseBuild") { cliBuild("Release") }
    tasks.register("install", Copy::class) {
        val installPath = System.getenv("TIX_INSTALL_PATH")
            ?: throw GradleException("TIX_INSTALL_PATH must be present in the environment")
        val buildTarget = nativeBuildTarget()
        val sourcePath = platforms
            .filter { it.name == buildTarget.targetName }
            .flatMap { it.binaries }
            .firstOrNull { it.buildType == NativeBuildType.RELEASE }
            ?.outputFile
            ?: throw GradleException("No release target for current platform (${nativeBuildTarget()})")

        println(installPath)
        from(sourcePath)
        into(File(installPath))
        rename(sourcePath.name, "tix")
    }
}

fun Task.cliBuild(buildType: String) {
    val target = nativeBuildTarget()
    val buildTaskNames = target.buildTaskNames(buildType)
    buildTaskNames.forEach {
        dependsOn(tasks.findByName(it))
    }
}

fun nativeBuildTarget() =
    when (System.getProperty("os.name")) {
        "Mac OS X" -> if (System.getProperty("os.arch") == "aarch64") {
            NativeBuildTarget.MacOSArm64
        } else {
            NativeBuildTarget.MacOSX64
        }

        "Linux" -> NativeBuildTarget.LinuxX64
        else -> throw GradleException("Host OS is not currently supported.")
    }

sealed class NativeBuildTarget(private val taskSuffix: String, val targetName: String) {
    object LinuxX64 : NativeBuildTarget("LinuxX64", "linuxX64")
    object MacOSX64 : NativeBuildTarget("MacosX64", "macosX64")
    object MacOSArm64 : NativeBuildTarget("MacosArm64", "macosArm64")

    override fun toString() = this::class.simpleName ?: ""

    fun buildTaskNames(buildType: String) = listOf(
        "compileKotlin${taskSuffix}",
        "link${buildType}Executable${taskSuffix}"
    )
}