dependencies {
    implementation(project(":wm-system:core"))
    implementation(project(":wm-system:configuration"))
    implementation(project(":wm-system:product"))
    implementation(project(":internal-api"))
}

tasks.bootJar {
    enabled = false
}

tasks.jar {
    enabled = true
}
