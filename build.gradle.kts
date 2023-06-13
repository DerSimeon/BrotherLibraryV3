plugins {
    id("java")
}

group = "lol.simeon"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.usb4java:usb4java-javax:1.3.0")
}