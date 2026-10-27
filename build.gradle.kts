import de.siphalor.jcyo.gradle.JcyoTask
import de.siphalor.minecraft_modding_toolkit.gradle.project_plugin.filter.JsonMergeFilterReader

plugins {
	java
	alias(mcLibs.plugins.smcmtk)
	alias(mcLibs.plugins.fabric.loom)
	alias(libs.plugins.licenser)
	alias(libs.plugins.jcyo)
}

group = "fakemousewheelie"
val archivesBaseName = "${project.name}-mc${properties["minecraft.version.descriptor"]}"
val shortVersion = "${properties["version"]}"
version = "${shortVersion}+mc${mcLibs.versions.minecraft.get()}"

license {
	rule(file("LICENSE_HEADER"))
	include("**/*.java")
}

repositories {
	maven {
		name = "shedaniel"
		url = uri("https://maven.shedaniel.me/")
	}
	maven {
		name = "TerraformersMC"
		url = uri("https://maven.terraformersmc.com/releases")
	}
	maven {
		name = "Nucleoid"
		url = uri("https://maven.nucleoid.xyz")
		mavenContent {
			includeGroupAndSubgroups("eu.pb4")
		}
	}
	maven {
		name = "ParchmentMC"
		url = uri("https://maven.parchmentmc.org")
		mavenContent {
			includeGroup("org.parchmentmc.data")
		}
	}
	maven {
		name = "Siphalor"
		url = uri("https://maven.siphalor.de/")
		mavenContent {
			includeGroupAndSubgroups("de.siphalor")
		}
	}
	maven {
		name = "Jitpack"
		url = uri("https://jitpack.io")
		mavenContent {
			includeGroupAndSubgroups("com.github")
		}
	}
	mavenLocal()
}

// Workaround for https://github.com/gradle/gradle/issues/10195
configurations {
	include {
		isTransitive = true
		dependencies.filter { !it.name.contains("bom") }.forEach {
			(it as ModuleDependency).isTransitive = false
		}
	}
}

smcmtk {
	useMojangMappings()
	createModConfigurations(listOf(sourceSets.main.get()))
}

dependencies {
	annotationProcessor(libs.lombok)
	compileOnly(libs.lombok)

	minecraft(mcLibs.minecraft)
	"modImplementation"(libs.fabric.loader)

	compileOnly(libs.jspecify)
	if (mcLibs.versions.minecraft.get() != "26.3") {
		compileOnly("org.lwjgl:lwjgl-glfw:3.3.3")
	}

	for (mod in listOfNotNull(
		"fabric-api-base",
		"fabric-events-interaction-v0",
		"fabric-item-api-v1",
		smcmtk.mcProps.getting("fabric.api.item_group").orNull,
		"fabric-lifecycle-events-v1",
		smcmtk.mcProps.getting("fabric.api.key_mapping").get(),
		"fabric-message-api-v1",
		"fabric-networking-api-v1",
		"fabric-resource-loader-v0",
		"fabric-registry-sync-v0",
		"fabric-screen-api-v1",
	)) {
		"modImplementation"(fabricApi.module(mod, mcLibs.versions.fabric.api.get()))
	}

	"modImplementation"(mcLibs.modmenu)

	include(mcLibs.bundles.config)
	"modApi"(mcLibs.bundles.config) {
		exclude(group = "net.fabricmc.fabric-api")
		exclude(group = "de.siphalor.amecs-api")
	}

	include(mcLibs.amecs.key.mapping.descriptions)
	"modImplementation"(mcLibs.amecs.key.mapping.descriptions) {
		exclude(group = "net.fabricmc.fabric-api")
	}
	include(mcLibs.amecs.mouse.inputs)
	"modImplementation"(mcLibs.amecs.mouse.inputs) {
		exclude(group = "net.fabricmc.fabric-api")
	}
	include(mcLibs.amecs.priority.key.mappings)
	"modImplementation"(mcLibs.amecs.priority.key.mappings) {
		exclude(group = "net.fabricmc.fabric-api")
	}
}

tasks.processResources {
    inputs.property("version", project.version)
	inputs.property("extraClientMixins", smcmtk.mcProps.getting("mixins.extra.client"))

	filesMatching("fabric.mod.json") {
		filter<JsonMergeFilterReader>("merge" to mapOf(
			"version" to project.version,
			"depends" to mapOf(
				smcmtk.mcProps.getting("fabric.api.key_mapping").get() to "*"
			)
		))
	}
	filesMatching("fakemousewheelie.mixins.json") {
		filter<JsonMergeFilterReader>("merge" to mapOf(
			"client" to (smcmtk.mcProps.getting("mixins.extra.client").orNull?.let {
				it.split(",").map { mixin -> mixin.trim() }
			} ?: listOf())
		))
	}
}

java {
	sourceCompatibility = JavaVersion.toVersion(mcLibs.versions.java.get())
	targetCompatibility = JavaVersion.toVersion(mcLibs.versions.java.get())
	withSourcesJar()
}

val jcyo = tasks.register<JcyoTask>("jcyo") {
	inputDirectory = file("src/main/java")
	variables = smcmtk.mcProps.map {
		it.filterKeys { key -> key.startsWith("preprocessor.") }.mapKeys { (key, _) -> key.substring("preprocessor.".length) }
	}
	importOrder = listOf(
		"",
		"net.minecraft",
		"\\#",
	)
}

tasks.compileJava {
	dependsOn(jcyo)
}
