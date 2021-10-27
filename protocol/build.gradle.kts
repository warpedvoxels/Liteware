plugins {
    kotlin("plugin.serialization") version "1.5.31"
}

dependencies {
    implementation("io.ktor:ktor-io:${Versioning.Ktor}")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:${Versioning.Serialization}")
}
