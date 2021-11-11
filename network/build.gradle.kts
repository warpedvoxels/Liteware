dependencies {
    api("io.ktor:ktor-network:${Versioning.Ktor}")
    api(project(":protocol"))
    api(project(":api"))
    api("dev.kord.cache:cache-api:${Versioning.KordCache}")
    api("dev.kord.cache:cache-map:${Versioning.KordCache}")
}