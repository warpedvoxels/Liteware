dependencies {
    api(project(":api"))
    api(project(":network"))
    api(project(":protocol"))
}

tasks.shadowJar {
    manifest {
        attributes(Pair("Main-Class", "me.hexalite.liteware.testing.LitewareBootstrapTestKt"))
    }
}