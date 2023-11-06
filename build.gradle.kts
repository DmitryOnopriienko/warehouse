import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.google.protobuf") version "0.9.4"
    kotlin("jvm") version "1.9.0"
}

allprojects {
    group = "com.ajaxproject"
    version = "0.0.1-SNAPSHOT"
    description = "warehouse"
    repositories {
        mavenCentral()
    }
    apply(plugin = "com.google.protobuf")
    apply(plugin = "kotlin")

    dependencies {
        implementation("com.google.protobuf:protobuf-java:3.24.3")

        implementation("io.projectreactor:reactor-core:3.5.11")

        implementation("io.grpc:grpc-core:1.59.0")
        implementation("io.grpc:grpc-protobuf:1.59.0")
        implementation("io.grpc:grpc-netty:1.59.0")
        implementation("io.grpc:grpc-stub:1.59.0")

        implementation("com.salesforce.servicelibs:reactor-grpc:1.2.4")
        implementation("com.salesforce.servicelibs:reactive-grpc-common:1.2.4")
        implementation("com.salesforce.servicelibs:reactor-grpc-stub:1.2.4")
    }
}

subprojects {
    apply(plugin = "kotlin")

    tasks.withType<KotlinCompile> {
        kotlinOptions {
            freeCompilerArgs += "-Xjsr305=strict"
            jvmTarget = "17"
        }
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_17
    }
}
