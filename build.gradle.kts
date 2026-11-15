plugins {
    java
}

group = "com.example.logintests"
version = "1.0.0"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("io.rest-assured:rest-assured:5.5.2")
    testImplementation("com.fasterxml.jackson.core:jackson-databind:2.18.2")
    testImplementation("dev.failsafe:failsafe:3.3.2")
    testImplementation("com.microsoft.playwright:playwright:1.49.0")
    testImplementation("org.testng:testng:7.10.2")
    testImplementation("org.assertj:assertj-core:3.26.3")
    testImplementation("org.apache.logging.log4j:log4j-api:2.24.3")
    testRuntimeOnly("org.apache.logging.log4j:log4j-core:2.24.3")
}

tasks.test {
    useTestNG()
    environment("PLAYWRIGHT_BROWSERS_PATH", layout.buildDirectory.dir("playwright-browsers").get().asFile.absolutePath)
    testLogging {
        events("passed", "skipped", "failed")
    }
}


tasks.register<Test>("apiTest") {
    useTestNG()
    filter {
        includeTestsMatching("com.example.logintests.tests.BookingApiTest")
    }
    listOf("api.baseUrl", "api.username", "api.password").forEach { key ->
        System.getProperty(key)?.let { systemProperty(key, it) }
    }
    testLogging {
        events("passed", "skipped", "failed")
    }
}
