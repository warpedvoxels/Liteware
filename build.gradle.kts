import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.6.0"
    kotlin("plugin.serialization") version "1.6.0"
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
        implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${Versioning.Serialization}")
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versioning.Coroutines}")
        implementation("org.jetbrains.kotlinx:kotlinx-datetime:${Versioning.DateTime}")
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