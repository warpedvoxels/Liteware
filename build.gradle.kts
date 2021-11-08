import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.5.31"
}

allprojects {
    apply(plugin = "org.jetbrains.kotlin.jvm")

    group = "me.hexalite"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }

    dependencies {
        testImplementation(kotlin("test"))
        implementation(kotlin("reflect"))
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versioning.Coroutines}")
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