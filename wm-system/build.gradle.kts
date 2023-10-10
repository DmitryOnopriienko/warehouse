plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    id("io.gitlab.arturbosch.detekt")
    kotlin("jvm")
    kotlin("plugin.spring")
    kotlin("plugin.allopen")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb:3.1.3")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("io.nats:jnats:2.16.14")
    implementation("com.google.protobuf:protobuf-java:3.24.3")

    implementation(project(":internal-api"))

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
