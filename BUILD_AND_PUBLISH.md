# Build and publish

Needs JDK 25 and internet access to maven.fabricmc.net, maven.siphalor.de, maven.terraformersmc.com etc.

    ./gradlew build          # Windows: gradlew.bat build
    -> build/libs/fakemousewheelie-1.0.3+mc26.3.jar   (upload the jar WITHOUT "-sources"/"-dev")

Modrinth: create a project (Mod, Client side required/unsupported on server as you prefer, license Apache-2.0),
paste MODRINTH.md as the description, upload the jar as a version: loader Fabric, game version 26.3,
dependency Fabric API (required). Link the original project in the description (already done).

Known caveats
- The Siphalor libraries (Amecs, Coat, Tweed bridges) have no mc26.3.0 builds yet; gradle/mc-26.3.0/mc.versions.toml
  points at the mc26.2.0 artifacts. If they fail to resolve or crash at runtime, either build the libs for 26.3 from
  source (github.com/Siphalor) or wait for upstream builds, then change the suffix.
- Libraries are bundled (jar-in-jar) by the existing Gradle setup; verify the jar contains META-INF/jars.
- The bundled Coat help cursor uses the default cursor on 26.3 to avoid its obsolete GLFW implementation.
- If 26.3 changed Minecraft APIs used by this mod, compile errors will point at the spots; the preprocessor
  switch is `//# if MC_VERSION_NUMBER >= 260300` (set in gradle/mc-26.3.0/gradle.properties).
- Test: start the game, open inventory, scroll/sort, then quit from title screen and from in-world.
