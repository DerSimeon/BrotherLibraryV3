plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
    `maven-publish`
}

group = "lol.simeon"
version = "1.2-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.usb4java:usb4java-javax:1.3.0")
}

publishing {
    repositories {
        maven("https://repo.simeon.lol/snapshots/"){
            credentials {
                username = "UNK"
                password = "UNK"
            }
        }
    }
    publications {
        create<MavenPublication>("build") {
            from(components["java"])
        }
    }
}
