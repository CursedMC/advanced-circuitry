@file:Suppress("DEPRECATION")

import com.modrinth.minotaur.TaskModrinthUpload
import com.modrinth.minotaur.request.VersionType

plugins {
	id("org.jetbrains.kotlin.jvm") version "1.5.0"
	id("fabric-loom") version "0.11.+"
	id("com.modrinth.minotaur") version "1.2.1"
	`maven-publish`
}

val modId: String by project
val minecraftVersion: String by project
val loaderVersion: String by project
val yarnMappings: String by project
val fabricVersion: String by project
val modVersion: String by project
val mavenGroup: String by project
val fabricKotlinVersion: String by project
val modLoader: String by project
val modrinthId: String by project
val verType: String by project

base.archivesBaseName = modId
version = modVersion
group = mavenGroup

repositories {
	mavenLocal()
	
	maven {
		name = "Curseforge Maven"
		url = uri("https://www.cursemaven.com")
	}
	
	maven {
		name = "Devan-Kerman/Devan-Repo"
		url = uri("https://raw.githubusercontent.com/Devan-Kerman/Devan-Repo/master/")
	}
	
	maven {
		name = "Bikeshedaniel Maven"
		url = uri("https://maven.shedaniel.me/")
	}
	
	maven {
		name = "Modrinth Maven"
		url = uri("https://api.modrinth.com/maven/")
		content {
			includeGroup("maven.modrinth")
		}
	}
	
	// hacky fix until terraformers mc maven gets fixed
	/*if (System.getenv("GITHUB_ACTIONS") == null) {
		maven {
			name = "TerraformersMC"
			url = uri("https://maven.terraformersmc.com/")
		}
	}*/
	
	maven {
		name = "JitPack"
		url = uri("https://jitpack.io")
	}
	
	maven {
		url = uri("https://bai.jfrog.io/artifactory/maven")
	}
}

val modImplementationInclude by configurations.register("modImplementationInclude")

dependencies {
	minecraft("net.minecraft", "minecraft", minecraftVersion)
	mappings("net.fabricmc", "yarn", yarnMappings, classifier = "v2")
	
	modImplementation("net.fabricmc", "fabric-loader", loaderVersion)
	modImplementation("net.fabricmc.fabric-api", "fabric-api", fabricVersion)
	modImplementation("net.fabricmc", "fabric-language-kotlin", fabricKotlinVersion)
	
//	modImplementation("net.devtech", "grossfabrichacks", "6.1")
//	modImplementation("net.devtech", "arrp", "0.+")
	modImplementation("maven.modrinth", "libreg", "0.2.5+1.18.2")
	
	if (!file("ignoreruntimes.txt").exists()) {
		println("Setting up runtimes...")
		
		modRuntimeOnly("com.terraformersmc", "modmenu", "3.1.0")
		modRuntimeOnly("maven.modrinth", "wthit", "fabric-4.8.0")
		modRuntimeOnly("maven.modrinth", "sodium", "mc1.18.2-0.4.1")
		
		modRuntimeOnly("org.joml:joml:1.10.2") // sodium dependency
//		include("org.joml:joml:1.10.2")
		
		modRuntimeOnly("maven.modrinth", "lithium", "mc1.18.2-0.7.9")
		modRuntimeOnly("maven.modrinth", "phosphor", "mc1.18.x-0.8.1")
		modRuntimeOnly("maven.modrinth", "sodium-extra", "mc1.18.2-0.4.2")
		modRuntimeOnly("maven.modrinth", "reeses-sodium-options", "mc1.18.2-1.4.2")
//		modRuntimeOnly("maven.modrinth", "dashloader", "3.1.1+1.18") // broken in dev env
//		modRuntimeOnly("maven.modrinth", "krypton", "0.1.9") // broken in dev env
		modRuntimeOnly("maven.modrinth", "lazydfu", "0.1.2")
	}
	
//	add(sourceSets.main.get().getTaskName("mod", JavaPlugin.IMPLEMENTATION_CONFIGURATION_NAME), modImplementationInclude)
//	add(, modImplementationInclude)
}

java {
	sourceCompatibility = JavaVersion.VERSION_17
	targetCompatibility = JavaVersion.VERSION_17
}

publishing {
	publications {
	
	}
}

tasks.withType<JavaCompile> {
	options.encoding = "UTF-8"
	
	options.release.set(17)
}

tasks.withType<AbstractArchiveTask> {
	from(file("LICENSE"))
}

tasks.processResources {
	filesMatching("fabric.mod.json") {
		expand("version" to project.version)
	}
}

tasks.register("publishModrinth", TaskModrinthUpload::class) {
	onlyIf {
		System.getenv("MODRINTH") != null
	}
	
	token = System.getenv("MODRINTH")
	projectId = modrinthId
	uploadFile = tasks.remapJar.get().archiveFile.get().asFile
	addGameVersion(minecraftVersion)
	addLoader(modLoader)
	versionType = VersionType.valueOf(verType)
	versionNumber = version as String
//	versionName = "${rootProject.name} ${versionNumber.split('+')[0]} for Minecraft $minecraftVersion"
	versionName = File("./modrinth/name.txt").readText()
	changelog = File("./modrinth/CHANGELOG.md").readText()
}.get().dependsOn(tasks.build.get())
