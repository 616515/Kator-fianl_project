val kotlin_version: String by project
val logback_version: String by project

plugins {
    kotlin("jvm") version "2.1.10"
    id("io.ktor.plugin") version "3.2.1"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.23"
}

group = "com.example"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")
}

tasks.withType<JavaExec> {
    if (name == "run") {
        systemProperty("ktor.application.modules", "com.movielibrary.ApplicationKt.module")
    }
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core")
    implementation("io.ktor:ktor-server-content-negotiation")
    implementation("io.ktor:ktor-server-netty")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("io.ktor:ktor-server-test-host")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlin_version")

    // Ktor Server Core & Engine
    implementation("io.ktor:ktor-server-core")
    implementation("io.ktor:ktor-server-netty")

    // Ktor Plugins
    implementation("io.ktor:ktor-server-content-negotiation") // สำหรับการแปลง JSON
    implementation("io.ktor:ktor-server-status-pages")       // สำหรับจัดการ Error Pages

    // kotlinx.serialization library for JSON processing
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")

    // สำหรับ Logging (logback) - ใช้เวอร์ชันที่เข้ากันได้กับ Ktor 3.x
    implementation("ch.qos.logback:logback-classic:1.5.6") // กำหนดเวอร์ชันตรงๆ

    // Test dependencies
    testImplementation("io.ktor:ktor-server-test-host") // Ktor's test client
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5") // Kotlin test framework for JUnit 5
    testImplementation("io.ktor:ktor-client-content-negotiation") // Client-side JSON handling for tests
    testImplementation("io.ktor:ktor-client-core") // Ktor HTTP client core
    testImplementation("io.ktor:ktor-client-apache") // หรือ client engine อื่นๆ เช่น ktor-client-cio, ktor-client-netty
    testImplementation("io.mockk:mockk:1.13.11") // Mocking library
}
