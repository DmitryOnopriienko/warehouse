plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    id("io.gitlab.arturbosch.detekt")
    kotlin("jvm")
    kotlin("plugin.spring")
    kotlin("plugin.allopen")
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://packages.confluent.io/maven/")
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webflux:3.1.4")
    implementation("org.springframework.boot:spring-boot-starter-validation:3.1.3")

    implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive:3.1.4")
    implementation("org.springframework.boot:spring-boot-starter-data-redis-reactive:3.1.5")

    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions:1.2.2")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.2")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.9.0")

    implementation("io.nats:jnats:2.16.14")
    implementation("com.google.protobuf:protobuf-java:3.24.3")
    implementation("org.springframework.kafka:spring-kafka:3.0.12")
    implementation("io.projectreactor.kafka:reactor-kafka:1.3.21")
    implementation("io.confluent:kafka-protobuf-serializer:7.5.1")
    implementation("io.confluent:kafka-schema-registry-maven-plugin:7.5.1")

    implementation(platform("io.projectreactor:reactor-bom:2022.0.12"))
    implementation("io.projectreactor:reactor-core")

    implementation(project(":internal-api"))

    testImplementation("org.springframework.boot:spring-boot-starter-test:3.1.3")
    testImplementation("io.projectreactor:reactor-test:3.5.11")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
}

tasks.withType<Test> {
    useJUnitPlatform()
}
