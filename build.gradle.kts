plugins {
    kotlin("jvm") version "1.9.20"
}

repositories {
    mavenCentral()
}

sourceSets {
    main {
        java.srcDirs("src")
    }
}

dependencies {
    implementation("org.jgrapht", "jgrapht-core", "1.5.0")
}

tasks {
    wrapper {
        gradleVersion = "8.4"
    }
}
