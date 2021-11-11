import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.31"
    kotlin("plugin.serialization") version "1.5.31"
    id("com.github.johnrengelman.shadow") version "7.1.0"
}

allprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")
    apply(plugin = "org.jetbrains.kotlin.plugin.serialization")
    apply(plugin = "com.github.johnrengelman.shadow")

    tasks.shadowJar {
        minimize()
        archiveFileName.set("liteware-${archiveFileName.get()}")
        from(rootProject.fileTree("licenses"))
    }

    repositories {
        mavenCentral()
    }

    dependencies {
        testImplementation(kotlin("test"))
        api(kotlin("reflect"))
        api("org.jetbrains.kotlinx:kotlinx-serialization-json:${Versioning.Serialization}")
        api("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versioning.Coroutines}")
    }

    tasks.test {
        useJUnitPlatform()
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            jvmTarget = "16"
            freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
        }
    }
}