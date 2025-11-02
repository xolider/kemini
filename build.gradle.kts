import org.jetbrains.dokka.gradle.engine.parameters.VisibilityModifier

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.dokka)
}

kotlin {
    val os = System.getProperty("os.name").lowercase()
    val arch = System.getProperty("os.arch").lowercase()
    val target = when {
        os.startsWith("windows") -> when(arch) {
            "amd64" -> mingwX64()
            else -> throw Exception("Unsupported architecture $arch")
        }
        os.startsWith("mac") -> when(arch) {
            "amd64" -> macosX64()
            "aarch64" -> macosArm64()
            else -> throw Exception("Unsupported architecture $arch")
        }
        os.startsWith("linux") -> when(arch) {
            "amd64" -> linuxX64()
            "aarch64" -> linuxArm64()
            else -> throw Exception("Unsupported architecture: $arch")
        }
        else -> throw Exception("Unsupported OS: $os")
    }

    target.run {
        binaries.executable {
            entryPoint = "dev.vicart.kemini.main"
        }
    }

    sourceSets {
        nativeMain.dependencies {
            implementation(libs.ktor.client.core)
            implementation(libs.kotlinx.serialization.properties)
            implementation(libs.ktor.client.content.negotiation)
            implementation(libs.ktor.serialization.kotlinx.json)
        }

        linuxMain.dependencies {
            implementation(libs.ktor.client.curl)
        }
        mingwMain.dependencies {
            implementation(libs.ktor.client.winhttp)
        }
        macosMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
    }
}

dokka {
    dokkaSourceSets.configureEach {
        documentedVisibilities.set(listOf(VisibilityModifier.Private) + documentedVisibilities.get())
    }
}