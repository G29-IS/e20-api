plugins {
    application
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.shadow)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ktor)
    alias(libs.plugins.ksp)
}

group = "app.e_20"
version = "0.0.1"
application {
    mainClass.set("app.e_20.ApplicationKt")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.bundles.logging)
    implementation(libs.reflections)
    api(libs.slf4j.api)

    implementation(libs.kotlinx.datetime)

    ksp(libs.koin.ksp)
    implementation(libs.bundles.koin)

    implementation(libs.bundles.ktor.server)
    implementation(libs.bundles.ktor.client)
    implementation(libs.bundles.spring.security)
    implementation(libs.bundles.monitoring)

    implementation(libs.bundles.postgres)

    implementation(libs.kotlinx.coroutines)
    implementation(libs.dotenv)
    implementation(libs.jedis)
    implementation(libs.google.api.client)

    testImplementation(libs.junit)
    testImplementation(kotlin("test"))
}

ksp {
    arg("KOIN_CONFIG_CHECK","true")
}

sourceSets.main {
    java.srcDirs("build/generated/ksp/main/kotlin")
}

tasks.test {
    useJUnitPlatform()
}

tasks {
    shadowJar {
        archiveFileName.set("e20-api.jar")
        mergeServiceFiles()
    }
}