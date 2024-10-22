plugins {
    id("java-library")
    id("idea")
    id("maven-publish")
    id("net.neoforged.moddev") version "2.0.42-beta"
}

val mod_version = properties["mod_version"] as String
val mod_group_id = properties["mod_group_id"] as String
val mod_id = properties["mod_id"] as String
val neo_version = properties["neo_version"] as String

version = mod_version
group = mod_group_id


repositories {
    mavenLocal()
}

base {
    archivesName = mod_id + "_neoforge"
}

java.toolchain.languageVersion = JavaLanguageVersion.of(21)

neoForge {
    // Specify the version of NeoForge to use.
    version = neo_version

    runs {
        create("client") {
            client()
        }
    }

    mods {
        create(mod_id) {
            sourceSet(sourceSets.main.get())
        }
    }
}

dependencies {
    implementation(rootProject.sourceSets.main.get().allSource)
}

tasks.jar {
    dependsOn(rootProject.tasks.named("processResources"))
    destinationDirectory = rootDir.toPath().resolve("build").resolve("libs_forge").toFile()
    from(rootProject.sourceSets.main.get().output.classesDirs)
    from(rootProject.sourceSets.main.get().output.resourcesDir!!) {
        exclude("fabric.mod.json")
    }
}
