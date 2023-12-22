import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget

plugins {
    kotlin("multiplatform")
    id("tz.co.asoft.library")
    alias(kotlinz.plugins.compose)
    id("com.jakewharton.mosaic") version "0.10.0"
    application
}

description = "Primary building block needed for running concurrent multiplatform code"

application {
    mainClass = "kit.MainKt"
}

mosaic {
    kotlinCompilerPlugin = kotlinz.versions.compose.compiler
}

kotlin {
    if (Targeting.JVM) jvm {
        withJava()
        library()
    }
    val osx = if (Targeting.OSX) osxTargets() else listOf()
    val linux = if (Targeting.LINUX) linuxTargets() else listOf()
    val windows = if (Targeting.MINGW) mingwTargets() else listOf()

    val native = osx + linux + windows

    targets.withType<KotlinNativeTarget>().configureEach {
        binaries.executable {
            entryPoint = "kit.main"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
            }
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
        }

        val nativeMain by creating {
            dependsOn(commonMain)
        }

        native.forEach {
            val main by it.compilations.getting
            main.defaultSourceSet { dependsOn(nativeMain) }
        }
    }
}