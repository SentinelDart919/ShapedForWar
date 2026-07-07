package b919.SFWar.content;

import arc.graphics.Color;
import b919.SFWar.planets.MoonGenerator;
import b919.SFWar.planets.TerraPlanetGenerator;
import mindustry.game.Team;
import mindustry.graphics.g3d.*;
import mindustry.type.Planet;

public class SFWarPlanets {
    public static Planet
            elsol,
            terraPlanet,
            terraMoon;
    public static void load(){
        elsol = new Planet("old-sun", null, 4f){{
            orbitRadius = 10000;
            bloom = true;
            accessible = false;
            visible = true;
            alwaysUnlocked = true;
            meshLoader = () -> new SunMesh(
                    this, 7,
                    6, 0.3, 1.7, 1.2, 1,
                    1.1f,
                    Color.valueOf("ff6e38"),
                    Color.valueOf("ff7a38"),
                    Color.valueOf("ff9638"),
                    Color.valueOf("ffc64c"),
                    Color.valueOf("ffc64c"),
                    Color.valueOf("ffe371"),
                    Color.valueOf("f4ee8e"),
                    Color.valueOf("faffb9"),
                    Color.valueOf("f6ffd5")

            );
        }};
        terraPlanet = new Planet("terra", elsol, 1f, 3){{
            generator = new TerraPlanetGenerator();
            cloudMeshLoader = () -> new MultiMesh(
                    new HexSkyMesh(this, 11, 0.15f, 0.13f, 7,  Color.valueOf("101011").mul(0.9f).a(0.75f), 2, 0.45f, 0.9f, 0.38f),
                    new HexSkyMesh(this, 7, 0.6f, 0.16f, 7, Color.valueOf("1a1c1e").cpy().a(0.75f), 2, 0.45f, 1f, 0.41f),
                    new HexSkyMesh(this, 9, 0.85f, 0.18f, 7, Color.valueOf("353c44").cpy().a(0.75f), 3, 0.45f, 1f, 0.41f),
                    new HexSkyMesh(this, 12, 1.1f, 0.20f, 7, Color.valueOf("61666a").cpy().a(0.75f), 4, 0.45f, 1f, 0.41f)
            );
            meshLoader = () -> new HexMesh(this, 6);

            allowCampaignRules = true;
            //defaultCore = RebornBlocks.primitiveCoreShard;
            alwaysUnlocked = true;
            drawOrbit = true;
            orbitSpacing = 1.2f;
            sectorSeed = 919;
            allowWaves = true;
            allowLegacyLaunchPads = true;
            allowSectorInvasion = true;
            allowLaunchToNumbered = true;
            allowLaunchSchematics = false;
            enemyCoreSpawnReplace = true;
            allowLaunchLoadout = true;

            ruleSetter = r -> {
                r.waveTeam = Team.blue;
                r.placeRangeCheck = false;
                //r.showSpawns = true; - compile error idk why
                r.coreDestroyClear = true;
            };
            showRtsAIRule = true;
            iconColor = Color.valueOf("49515c");
            atmosphereColor = Color.valueOf("20282b");
            atmosphereRadIn = 0.02f;
            atmosphereRadOut = 0.3f;
            startSector = 8;
            allowSelfSectorLaunch = true;
            landCloudColor = Color.sky;
        }};
        terraMoon = new Planet("terra-moon", terraPlanet, 0.35f, 1){{
            generator = new MoonGenerator();
            orbitRadius = 5.3f;
            meshLoader = () -> new HexMesh(this, 4);
            allowCampaignRules = true;
            //defaultCore = RebornBlocks.primitiveCoreShard;
            alwaysUnlocked = true;
            drawOrbit = true;
            orbitSpacing = 3.2f;
            sectorSeed = 919;
            allowWaves = true;
            allowLegacyLaunchPads = true;
            allowSectorInvasion = true;
            allowLaunchToNumbered = true;
            allowLaunchSchematics = false;
            enemyCoreSpawnReplace = true;
            allowLaunchLoadout = true;
            startSector = 8;
            allowSelfSectorLaunch = true;
            hasAtmosphere = false;
        }};
    }
}
