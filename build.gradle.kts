import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose")
}

group = "me.sudodios.plaudio"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    maven("https://jitpack.io")
    google()
}

dependencies {
    implementation(compose.desktop.currentOs)
    api(compose.foundation)
    api(compose.animation)
    api("moe.tlaster:precompose:1.5.4")

    implementation("dev.icerock.moko:mvvm-livedata-compose:0.16.1")

    implementation("net.jthink:jaudiotagger:3.0.1")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("uk.co.caprica:vlcj:4.8.2")

    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))
    implementation ("com.github.sealedtx:java-youtube-downloader:3.2.3")
}

compose.desktop {
    application {
        mainClass = "PlaudioKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb,TargetFormat.Exe)
            modules("java.sql")
            packageName = "Plaudio"
            packageVersion = "1.4.0"
            val iconsRoot = project.file("src/main/resources")
            linux {
                iconFile.set(iconsRoot.resolve("icons/app_icon.png"))
            }
            windows {
                iconFile.set(iconsRoot.resolve("icons/app_icon.ico"))
            }
        }
        buildTypes.release.proguard {
            configurationFiles.from(project.file("rules.pro").absolutePath)
        }
    }
}
