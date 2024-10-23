plugins {
    id("fabric-loom") version "1.6-SNAPSHOT"
    id("maven-publish")
}

java.sourceCompatibility = JavaVersion.VERSION_21
java.targetCompatibility = JavaVersion.VERSION_21

version = properties["mod_version"] as String
group = properties["maven_group"] as String
base.archivesName = "BetterAddServer-${properties["minecraft_version"]}-fabric"

repositories {

}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter:5.7.1")
    // To change the versions see the gradle.properties file
    minecraft("com.mojang:minecraft:${project.properties["minecraft_version"]}")
    mappings(loom.officialMojangMappings())
    modImplementation("net.fabricmc:fabric-loader:${project.properties["loader_version"]}")
}

tasks {
    processResources {
        inputs.property("version", project.version)

        filesMatching("fabric.mod.json") {
            expand("version" to project.version)
        }
        filesMatching("META-INF/neoforge.mods.toml") {
            expand("version" to project.version)
        }
    }

    test {
        useJUnitPlatform()
    }

    withType<JavaCompile> {
        options.release.set(java.targetCompatibility.majorVersion.toInt())
    }

    java {
        withSourcesJar()
    }

    jar {
        from("LICENSE") {
            rename { "${it}_${base.archivesName.get()}" }
        }
    }
}

loom {
    runs {
        named("client") {
            client()
            configName = "Fabric Client"
            ideConfigGenerated(true)
            runDir("run")
        }
    }

    @Suppress("UnstableApiUsage")
    mixin { defaultRefmapName.set("betteraddserver-fabric.refmap.json") }
}
