package b919.SFWar.content.blocks;

import arc.graphics.Color;
import b919.SFWar.content.SFWarBlocks;
import b919.SFWar.content.bullets.SuperRaulCannonBullet;
import b919.SFWar.content.units.NebulaeUnits;
import b919.SFWar.utils.SFWarFX;
import b919.SFWar.world.nebulae.blocks.power.NebulaePanel;
import b919.SFWar.world.production.MultiRecipeCrafter;
import b919.SFWar.world.production.Recipe;
import mindustry.content.*;
import mindustry.entities.UnitSorts;
import mindustry.entities.bullet.*;
import mindustry.entities.pattern.ShootBarrel;
import mindustry.gen.Sounds;
import mindustry.graphics.Pal;
import mindustry.type.Category;
import mindustry.world.Block;
import mindustry.game.*;
import mindustry.world.blocks.defense.ForceProjector;
import mindustry.world.blocks.defense.Wall;
import mindustry.world.blocks.defense.turrets.ItemTurret;
import mindustry.world.blocks.defense.turrets.LaserTurret;
import mindustry.world.blocks.defense.turrets.TractorBeamTurret;
import mindustry.world.blocks.production.AttributeCrafter;
import mindustry.world.blocks.production.Drill;
import mindustry.world.blocks.production.Fracker;
import mindustry.world.blocks.production.GenericCrafter;
import mindustry.world.blocks.storage.CoreBlock;
import mindustry.world.draw.*;
import mindustry.world.meta.Attribute;
import mindustry.world.meta.BuildVisibility;
import mindustry.type.*; // this will import the classes in this package but not the classes in subpackages
import b919.SFWar.content.SFWarItems;
import b919.SFWar.content.SFWarLiquids;
import mindustry.world.meta.Env;

import static mindustry.type.ItemStack.with;

public class NebulaeBlocks {
    public static Block
            // cores
        domusLucis, castellumLucis, arxLucis,
            // raw production
        greenStardustPlant, blueStardustFormationPlant,
            // Nyctar
        gaseousNyctarMixer, liquidNyctarMixer, nyctarSolidifier, nyctoSteelFoundry, pureNyctarRefinery,
            // Stardust
        stardustCrystallizer, purpleStardustGrinder, yellowStardustFoundry, redStardustManufactorer, idealizedStardustManifestationChamber,
            // Other
        calibrePress, soulCuller, oilBore, starKiln, heavyGraphitePress, nyctoSporeCultivator, dimensionDrill,
            // defensive
        luminosityCondenser,
            // walls
        blueCrystallizedStardustWall, blueCrystallizedStardustWallLarge, nyctoSteelWall, nyctoSteelWallLarge, nyctoSporeWall, nyctoSporeWallLarge, sporeWall, sporeWallLarge,
            // turrets
        crescentMoon, starScreech, solarEruption, meteoroid, comet, asteroid, moonlight,
            // "Supreme" turret
        heavenPiercer,
            // units, T1-3
        seminarumStellarum, sartrixNyctar,
            // units, T4-6
        cosmicCradle, magnaSartrixNyctar,
            // units, T7
        nexusStellarum, nyctarNexus,
            // Supreme unit
        magnumOpusArtesMagnae,
            // drills
        copperDrill, dustDrill, reinforcedDustDrill, crystalDrill, nyctoSteelDrill,
            // power
        nyxPanel, nyxPanelSmall;
    public static void load(){

        domusLucis = new CoreBlock("domus-lucis"){{
            requirements(Category.effect, BuildVisibility.shown, with(SFWarItems.solidifiedNyctar, 1000));
            health = 10800;
            itemCapacity = 17000;
            size = 6;

            unitCapModifier = 32;
            unitType = NebulaeUnits.blackWasp;
            // toxopid placeholder, replace with Nebulae core unit when possible
        }};
        castellumLucis = new CoreBlock("castellum-lucis"){{
            requirements(Category.effect, BuildVisibility.shown, with(SFWarItems.solidifiedNyctar, 5000, SFWarItems.nyctoSteel, 1000));
            health = 36750;
            itemCapacity = 21000;
            size = 7;

            unitCapModifier = 48;
            unitType = UnitTypes.toxopid;
        }};
        arxLucis = new CoreBlock("arx-lucis"){{
            requirements(Category.effect, BuildVisibility.shown, with());
            health = 96000;
            itemCapacity = 25000;
            size = 8;

            unitCapModifier = 64;
            unitType = UnitTypes.toxopid;
        }};
        luminosityCondenser = new ForceProjector("luminosity-condenser"){{
            requirements(Category.effect, BuildVisibility.shown, with());
            radius = 300f;
            sides = 20;
            shieldHealth = 3000;
            size = 4;
        }};
        nyxPanelSmall = new NebulaePanel("nyx-panel-small"){{
            requirements(Category.power, BuildVisibility.shown, with());
            // reverse solar, basically
            size = 1;
            powerProduction = 0.12f;
        }};
        nyxPanel = new NebulaePanel("nyx-panel"){{
            requirements(Category.power, BuildVisibility.shown, with());
            size = 3;
            powerProduction = 1.6f;
        }};
        copperDrill = new Drill("copper-drill"){{
            requirements(Category.production, with(Items.copper, 12));
            tier = 1;
            itemCapacity = 2*2*10;
            // drill tier is 1 for progression
            drillTime = 600;
            size = 2;

            consumeLiquid(Liquids.water, 0.05f).boost();
        }};
        greenStardustPlant = new GenericCrafter("green-stardust-plant"){{
            requirements(Category.crafting, with(Items.copper, 25, Items.lead, 10));
            outputItem = new ItemStack(SFWarItems.greenStardust, 1);
            craftTime = 120f;
            size = 2;
            hasPower = false;
            hasLiquids = false;

            consumeItems(with());
        }};
        dustDrill = new Drill("dust-drill"){{
            requirements(Category.production, with(SFWarItems.greenStardust, 12, SFWarItems.ferrum, 10));
            tier = 2;
            itemCapacity = 2*2*10;
            // drill tier is 2 for coal
            drillTime = 360;
            size = 2;

            consumeLiquid(Liquids.water, 0.15f).boost();
        }};
        starKiln = new MultiRecipeCrafter("star-kiln"){{
            requirements(Category.crafting, with(Items.copper, 60, SFWarItems.greenStardust, 30, Items.lead, 30));
            craftEffect = Fx.smeltsmoke;
            recipes.add(new Recipe[]{
                    new Recipe(20)
                            .consumeItems(ItemStack.with(Items.lead, 1, Items.sand, 1))
                            .outputItem(Items.metaglass, 1),
                    new Recipe(40)
                            .consumeItems(ItemStack.with(SFWarItems.ferrum, 3, Items.coal, 1))
                            .consumeLiquid(Liquids.water, 0.2f)
                            .outputItem(SFWarItems.steel, 3),
            });
            size = 2;
            hasPower = hasItems = true;
            drawer = new DrawMulti(new DrawDefault(), new DrawFlame(Color.valueOf("32dc3d")));
            ambientSound = Sounds.loopSmelter;
            ambientSoundVolume = 0.07f;
            consumePower(0.60f);
        }};
        blueStardustFormationPlant = new GenericCrafter("blue-stardust-formation-plant"){{
            requirements(Category.crafting, with(SFWarItems.greenStardust, 75, Items.copper, 50, Items.metaglass, 25));
            outputItem = new ItemStack(SFWarItems.blueStardust, 1);
            craftTime = 240f;
            liquidCapacity = 60f;
            size = 2;
            hasPower = hasLiquids;
            hasLiquids = true;

            consumeItems(with());
            consumePower(1f);
            consumeLiquid(Liquids.water, 0.1f);
        }};
        gaseousNyctarMixer = new GenericCrafter("gaseous-nyctar-mixer"){{
            requirements(Category.crafting, with());
            outputLiquid = new LiquidStack(SFWarLiquids.gaseousNyctar, 6f / 60f);
            craftTime = 60f;
            liquidCapacity = 60f;
            size = 2;
            hasLiquids = true;

            consumeItems(with(Items.coal, 1, SFWarItems.greenStardust, 1));
        }};
        reinforcedDustDrill = new Drill("reinforced-dust-drill"){{
            requirements(Category.production, with(SFWarItems.blueStardust, 10, SFWarItems.greenStardust, 10, Items.copper, 30));
            tier = 3;
            itemCapacity = 2*2*10;
            // drill tier is 3 for titanium
            drillTime = 270;
            size = 2;
            liquidBoostIntensity = 1.8f;

            consumeLiquid(Liquids.water, 0.05f).boost();
        }};
        heavyGraphitePress = new GenericCrafter("heavy-graphite-press"){{
            requirements(Category.crafting, with(Items.titanium, 75, Items.lead, 30));

            craftEffect = Fx.pulverizeMedium;
            outputItem = new ItemStack(Items.graphite, 8);
            craftTime = 90f;
            size = 3;
            hasItems = true;
            itemCapacity = 20;

            consumeItem(Items.coal, 10);
        }};
        stardustCrystallizer = new MultiRecipeCrafter("stardust-crystallizer"){{
            requirements(Category.crafting, with(SFWarItems.blueStardust, 50, SFWarItems.greenStardust, 150, Items.copper, 750, Items.titanium, 250, Items.metaglass, 125, Items.graphite, 325));
            consumePower(4f);
            size = 4;
            itemCapacity = 100;
            hasLiquids = true;
            liquidCapacity = 200;
            recipes.add(new Recipe[]{
                    new Recipe(20)
                            .consumeItems(ItemStack.with(SFWarItems.greenStardust, 1, Items.lead, 5))
                            .consumeLiquid(SFWarLiquids.gaseousNyctar, 0.1f)
                            .outputItem(SFWarItems.crystallizedGreenStardust, 1),
                    new Recipe(40)
                            .consumeItems(ItemStack.with(SFWarItems.blueStardust, 1, Items.titanium, 5))
                            .consumeLiquid(SFWarLiquids.gaseousNyctar, 0.2f)
                            .outputItem(SFWarItems.crystallizedBlueStardust, 1),
                    new Recipe(60)
                            .consumeItems(ItemStack.with(SFWarItems.purpleStardust, 1, Items.plastanium, 2, Items.sporePod, 3))
                            .consumeLiquid(SFWarLiquids.liquidNyctar, 0.25f)
                            .outputItem(SFWarItems.crystallizedPurpleStardust, 1),
                    new Recipe(80)
                            .consumeItems(ItemStack.with(SFWarItems.yellowStardust, 1, Items.surgeAlloy, 5))
                            .consumeLiquid(SFWarLiquids.liquidNyctar, 0.5f)
                            .outputItem(SFWarItems.crystallizedYellowStardust, 1),
            });
        }};
        blueCrystallizedStardustWall = new Wall("crystallized-blue-stardust-wall"){{
            requirements(Category.defense, with(SFWarItems.crystallizedBlueStardust, 6));
            health = 880;
        }};
        blueCrystallizedStardustWallLarge = new Wall("crystallized-blue-stardust-wall-large"){{
            requirements(Category.defense, ItemStack.mult(blueCrystallizedStardustWall.requirements, 4));
            health = 880 * 4;
            size = 2;
        }};
        purpleStardustGrinder = new GenericCrafter("purple-stardust-grinder"){{
            requirements(Category.crafting, with(SFWarItems.blueStardust, 50, SFWarItems.crystallizedGreenStardust, 75, Items.titanium, 100, Items.plastanium, 25));
            outputItem = new ItemStack(SFWarItems.purpleStardust, 1);
            craftTime = 360f;
            liquidCapacity = 60f;
            size = 3;
            hasPower = hasLiquids;
            hasLiquids = true;

            consumeItems(with(Items.titanium, 1, Items.lead, 1));
            consumePower(2f);
            consumeLiquid(Liquids.water, 0.2f);
        }};
        oilBore = new Fracker("oil-bore"){{
            requirements(Category.production, with(Items.copper, 150, Items.graphite, 175, Items.silicon, 75, SFWarItems.crystallizedBlueStardust, 50));
            result = Liquids.oil;
            updateEffect = Fx.pulverize;
            updateEffectChance = 0.05f;
            pumpAmount = 0.15f;
            size = 2;
            liquidCapacity = 40f;
            attribute = Attribute.oil;
            baseEfficiency = 0f;
            itemUseTime = 80f;

            consumeItem(Items.sand);
            consumePower(1f);
            consumeLiquid(SFWarLiquids.gaseousNyctar, 0.15f);
        }};
        liquidNyctarMixer = new GenericCrafter("liquid-nyctar-mixer"){{
            requirements(Category.crafting, with(SFWarItems.purpleStardust, 10, Items.plastanium, 40));
            outputLiquid = new LiquidStack(SFWarLiquids.liquidNyctar, 24f / 60f);
            craftTime = 60f;
            liquidCapacity = 60f;
            size = 2;
            hasLiquids = true;

            consumeItems(with(SFWarItems.blueStardust, 6));
            consumeLiquids(LiquidStack.with(Liquids.oil, 0.4f, SFWarLiquids.gaseousNyctar, 0.2f));
        }};
        nyctoSporeCultivator = new AttributeCrafter("nycto-spore-cultivator"){{
            requirements(Category.production, with(Items.plastanium, 25, SFWarItems.purpleStardust, 25, Items.silicon, 10, SFWarItems.chromium, 15));
            outputItem = new ItemStack(Items.sporePod, 2);
            craftTime = 75;
            size = 2;
            hasLiquids = true;
            hasPower = true;
            hasItems = true;
            liquidCapacity = 80f;

            craftEffect = Fx.none;
            envRequired |= Env.spores;
            attribute = Attribute.spores;

            ambientSound = Sounds.loopCultivator;
            ambientSoundVolume = 0.075f;

            legacyReadWarmup = true;
            drawer = new DrawMulti(
                    new DrawRegion("-bottom"),
                    new DrawLiquidTile(SFWarLiquids.liquidNyctar),
                    new DrawDefault(),
                    new DrawCultivator(),
                    new DrawRegion("-top")
            );
            maxBoost = 2f;

            consumePower(80f / 60f);
            consumeLiquid(Liquids.water, 18f / 60f);
        }};
        sporeWall = new Wall("spore-wall"){{
            requirements(Category.defense, with(Items.sporePod, 3));
            health = 1250;
        }};
        sporeWallLarge = new Wall("spore-wall-large"){{
            requirements(Category.defense, ItemStack.mult(sporeWall.requirements, 4));
            health = 1250 * 4;
            size = 2;
        }};
        crystalDrill = new Drill("crystal-drill"){{
            requirements(Category.production, with(SFWarItems.crystallizedBlueStardust, 20, SFWarItems.crystallizedGreenStardust, 30, SFWarItems.crystallizedPurpleStardust, 10));
            itemCapacity = 3*3*10;
            tier = 4;
            // drill tier 4 so cool
            drillTime = 180;
            size = 3;
            liquidBoostIntensity = 1.4f;

            consumeLiquid(SFWarLiquids.liquidNyctar, 0.1f).boost();
        }};
        nyctarSolidifier = new GenericCrafter("nyctar-solidifier"){{
            requirements(Category.crafting, with(SFWarItems.crystallizedPurpleStardust, 50, Items.plastanium, 75));
            outputItem = new ItemStack(SFWarItems.solidifiedNyctar, 2);
            craftTime = 60f;
            liquidCapacity = 60f;
            size = 2;
            hasPower = hasLiquids;
            hasLiquids = true;

            consumeItems(with(SFWarItems.crystallizedPurpleStardust, 1));
            consumePower(2f);
            consumeLiquid(SFWarLiquids.liquidNyctar, 0.2f);
        }};
        nyctoSporeWall = new Wall("nycto-spore-wall"){{
            requirements(Category.defense, with(Items.sporePod, 3, SFWarItems.solidifiedNyctar, 3));
            health = 2000;
        }};
        nyctoSporeWallLarge = new Wall("nycto-spore-wall-large"){{
            requirements(Category.defense, ItemStack.mult(nyctoSporeWall.requirements, 4));
            health = 2000 * 4;
            size = 2;
        }};
        yellowStardustFoundry = new GenericCrafter("yellow-stardust-foundry"){{
            requirements(Category.crafting, with(Items.surgeAlloy, 25, Items.titanium, 125, SFWarItems.solidifiedNyctar, 75));
            outputItem = new ItemStack(SFWarItems.yellowStardust, 1);
            craftTime = 480f;
            liquidCapacity = 60f;
            size = 3;
            hasPower = true;

            consumeItems(with(Items.surgeAlloy, 1));
            consumePower(4f);
        }};
        soulCuller = new GenericCrafter("soul-culler"){{
            requirements(Category.production, with(SFWarItems.crystallizedYellowStardust, 200, SFWarItems.chromium, 300));
            outputItem = new ItemStack(SFWarItems.soul, 1);
            craftTime = 600f;
            size = 5;
            consumeItems(ItemStack.with(SFWarItems.crystallizedYellowStardust, 10, Items.sporePod, 10));
            consumeLiquid(SFWarLiquids.liquidNyctar, 1f);
            consumePower(11.1f);
        }};
        dimensionDrill = new MultiRecipeCrafter("dimension-Drill"){{
            requirements(Category.production, with(SFWarItems.crystallizedYellowStardust, 200, SFWarItems.chromium, 300));
            size = 5;
            itemCapacity = 120;
            recipes.add(new Recipe[]{
                    new Recipe(180)
                            .consumeItems(ItemStack.with(SFWarItems.soul, 1, Items.lead, 1))
                            .outputItem(Items.copper, 40),
                    new Recipe(180)
                            .consumeItems(ItemStack.with(SFWarItems.soul, 1, Items.coal, 1))
                            .outputItem(Items.lead, 40),
                    new Recipe(180)
                            .consumeItems(ItemStack.with(SFWarItems.soul, 1, Items.copper, 1))
                            .outputItem(Items.titanium, 40),
                    new Recipe(180)
                            .consumeItems(ItemStack.with(SFWarItems.soul, 1, Items.titanium, 1))
                            .outputItem(Items.coal, 40),
                    new Recipe(180)
                            .consumeItems(ItemStack.with(SFWarItems.soul, 1, Items.metaglass, 1))
                            .outputItem(Items.silicon, 40),
                    new Recipe(180)
                            .consumeItems(ItemStack.with(SFWarItems.soul, 1, Items.silicon, 1))
                            .outputItem(Items.metaglass, 40),
                    new Recipe(180)
                            .consumeItems(ItemStack.with(SFWarItems.soul, 1, SFWarItems.ferrum, 1))
                            .outputItem(SFWarItems.ferrum, 40),
                    new Recipe(180)
                            .consumeItems(ItemStack.with(SFWarItems.soul, 1, SFWarItems.chromium, 1))
                            .outputItem(SFWarItems.chromium, 40),
                    new Recipe(180)
                            .consumeItems(ItemStack.with(SFWarItems.soul, 1, Items.graphite, 1))
                            .consumeLiquid(Liquids.water, 0.5f)
                            .outputItem(Items.tungsten, 40),
            });
            consumePower(11.1f);
        }};
        redStardustManufactorer = new GenericCrafter("red-stardust-manufactorer"){{
            requirements(Category.crafting, with(SFWarItems.ruby, 25, SFWarItems.solidifiedNyctar, 500, Items.pyratite, 50, Items.blastCompound, 75, SFWarItems.crystallizedYellowStardust, 25));
            outputItem = new ItemStack(SFWarItems.redStardust, 1);
            craftTime = 600f;
            liquidCapacity = 60f;
            size = 4;
            hasPower = hasLiquids;
            hasLiquids = true;
            consumeItems(with(Items.blastCompound, 1, SFWarItems.ruby, 1, SFWarItems.soul, 1));
            consumePower(11.1f);
            consumeLiquids(LiquidStack.with(Liquids.cryofluid, 0.1f, SFWarLiquids.liquidNyctar, 0.1f));
        }};
        nyctoSteelFoundry = new GenericCrafter("nyctosteel-foundry"){{
            requirements(Category.crafting, with(SFWarItems.crystallizedYellowStardust, 50, SFWarItems.solidifiedNyctar, 75));
            outputItem = new ItemStack(SFWarItems.nyctoSteel, 2);
            itemCapacity = 2*2*10;
            craftTime = 120f;
            liquidCapacity = 60f;
            size = 5;
            hasPower = hasLiquids;
            hasLiquids = true;

            consumeItems(with(SFWarItems.crystallizedYellowStardust, 6, SFWarItems.solidifiedNyctar, 8, Items.tungsten, 10));
            consumePower(500f / 60f);
            consumeLiquids(LiquidStack.with(SFWarLiquids.liquidNyctar, 0.5f, Liquids.cryofluid, 0.5f));
        }};
        nyctoSteelDrill = new Drill("nycto-steel-drill"){{
            requirements(Category.production, with(SFWarItems.nyctoSteel, 10, SFWarItems.solidifiedNyctar, 40));
            itemCapacity = 4*4*10;
            tier = 5;
            // drill tier 5 even cooler
            drillTime = 135;
            size = 4;
            liquidBoostIntensity = 1.6f;

            consumeLiquid(SFWarLiquids.liquidNyctar, 1.0f).boost();
        }};
        nyctoSteelWall = new Wall("nycto-steel-wall"){{
            requirements(Category.defense, with(SFWarItems.nyctoSteel, 12));
            health = 10000;
        }};
        nyctoSteelWallLarge = new Wall("nycto-steel-wall-large"){{
            requirements(Category.defense, ItemStack.mult(nyctoSteelWall.requirements, 4));
            health = 10000 * 4;
            size = 2;
        }};
        calibrePress = new GenericCrafter("calibre-press"){{
            requirements(Category.crafting, with(SFWarItems.nyctoSteel, 750, SFWarItems.nyctar, 75, SFWarItems.redStardust, 175));
            outputItem = new ItemStack(SFWarItems.heavenPiercingShell, 1);
            itemCapacity = 250;
            craftTime = 1800f;
            size = 2;
            hasPower = true;
            hasLiquids = false;

            consumeItems(with(SFWarItems.nyctoSteel, 250, SFWarItems.nyctar, 20, SFWarItems.redStardust, 100, Items.blastCompound, 30));
            consumePower(25f);
        }};
        idealizedStardustManifestationChamber = new GenericCrafter("idealized-stardust-manifestation-chamber"){{
            requirements(Category.crafting, with(Items.copper, 1, Items.lead, 1, Items.coal, 1, Items.graphite, 1, Items.silicon, 1, SFWarItems.ruby, 1));
            outputItem = new ItemStack(SFWarItems.idealizedStardust, 1);
            // copper as a placeholder since I don't want to edit items since you said you had commits not done yet
            // this is the gatekeeper item for the supreme unit :3
            craftTime = 3600f;
            liquidCapacity = 60f;
            size = 6;
            hasPower = hasLiquids;
            hasLiquids = true;
            // Dumbass long name for the white stardust. Exclusively for Magnum Opus; Artes Magnae and its lone unit.
            // placeholder consumption
            consumeItems(with(Items.lead, 1000*1000));
            consumePower(1000000f / 60f);
            consumeLiquids(LiquidStack.with(Liquids.cryofluid, 0.1f, SFWarLiquids.liquidNyctar, 0.1f));
        }};
        crescentMoon = new ItemTurret("crescent-moon"){{
            float brange = range = 300f;

            requirements(Category.turret, with(Items.silicon, 250, SFWarItems.solidifiedNyctar, 100));
            ammo(
                    SFWarItems.crystallizedBlueStardust, new RailBulletType(){{
                        trailColor = Color.valueOf("4684c7");
                        shootEffect = SFWarFX.instShootCrescent;
                        hitEffect = SFWarFX.instHitCrescent;
                        pierceEffect = Fx.railHit;
                        smokeEffect = Fx.smokeCloud;
                        pointEffect = SFWarFX.instTrailCrescent;
                        despawnEffect = SFWarFX.instBombCrescent;
                        pointEffectSpace = 8f;
                        damage = 350;
                        buildingDamageMultiplier = 0.2f;
                        pierceDamageFactor = 0.8f;
                        length = brange;
                        hitShake = 3f;
                        ammoMultiplier = 1f;
                    }}
            );

            maxAmmo = 42;
            ammoPerShot = 3;
            rotateSpeed = 2f;
            reload = 100f;
            ammoUseEffect = Fx.casing3Double;
            recoil = 2f;
            cooldownTime = reload;
            shake = 4f;
            size = 2;
            shootCone = 2f;
            shootSound = Sounds.shootForeshadow;
            unitSort = UnitSorts.strongest;
            envEnabled |= Env.space;

            coolantMultiplier = 0.6f;
            liquidCapacity = 60f;
            scaledHealth = 350;

            coolant = consumeCoolant(1f);
            depositCooldown = 2.0f;
            consumePower(10f);
        }};
        starScreech = new ItemTurret("star-screech"){{
            requirements(Category.turret, with(Items.graphite, 350, SFWarItems.solidifiedNyctar, 625, SFWarItems.nyctoSteel, 250, Items.silicon, 300));
            ammo(
                    SFWarItems.solidifiedNyctar, new MissileBulletType(4.2f, 12){{
                        width = 12f;
                        height = 12f;
                        shrinkY = 0f;
                        homingPower = 0.15f;
                        homingRange = 250f;
                        splashDamageRadius = 50f;
                        splashDamage = 50f;
                        hitEffect = Fx.blastExplosion;
                        despawnEffect = Fx.blastExplosion;
                        ammoMultiplier = 4f;
                        status = StatusEffects.tarred;

                        hitColor = backColor = trailColor = Color.valueOf("ab1ae0");
                        frontColor = Color.valueOf("8e00c2");
                    }},
                    SFWarItems.nyctoSteel, new MissileBulletType(4.2f, 225){{
                        width = 8f;
                        height = 8f;
                        shrinkY = 0f;
                        homingPower = 0.25f;
                        homingRange = 350f;
                        splashDamageRadius = 35f;
                        splashDamage = 225f * 0.2f;
                        hitEffect = Fx.blastExplosion;
                        despawnEffect = Fx.blastExplosion;
                        ammoMultiplier = 4f;
                        status = StatusEffects.slow;

                        hitColor = backColor = trailColor = Color.valueOf("8e00c2");
                        frontColor = Color.valueOf("ab1ae0");
                    }}
            );

            shoot = new ShootBarrel(){{
                barrels = new float[]{
                        -8, 6 -2.5f, 0,
                        -4, 6 -1.25f, 0,
                        0, 6, 0,
                        4, 6 -1.25f, 0,
                        8, 6 -2.5f, 0
                };
                shots = 5;
                shotDelay = 3f;
            }};

            shootY = 4.5f;
            reload = 60f * 4f / 7f;
            inaccuracy = 5f;
            range = 360f;
            consumeAmmoOnce = false;
            size = 4;
            scaledHealth = 480;
            shootSound = Sounds.shootMissile;
            envEnabled |= Env.space;
            coolantMultiplier = 0.3f;

            limitRange(5f);
            coolant = consumeCoolant(0.3f);
            depositCooldown = 2.0f;
        }};
        meteoroid = new ItemTurret("meteoroid"){{

            requirements(Category.turret, with(Items.copper, 25, Items.lead, 25, SFWarItems.greenStardust, 50));
            ammo(
                    Items.copper, new MissileBulletType(2f*3f, 25){{
                        width = 20f;
                        height = 20f;
                        shrinkY = 0f;
                        homingPower = 0.10f;
                        homingRange = 400f;
                        lifetime = 300f;
                        buildingDamageMultiplier = 0.2f;
                        pierceDamageFactor = 0.8f;
                        pierceCap = 5;
                        ammoMultiplier = 2f;
                        collidesAir = false;

                        hitColor = backColor = trailColor = Pal.copperAmmoBack;
                        frontColor = Pal.copperAmmoFront;
                    }},
                    Items.lead, new MissileBulletType(2.6f*3f, 25){{
                        width = 20f;
                        height = 20f;
                        shrinkY = 0f;
                        homingPower = 0.14f;
                        homingRange = 500f;
                        lifetime = 300f;
                        buildingDamageMultiplier = 0.2f;
                        pierceDamageFactor = 0.8f;
                        pierceCap = 5;
                        ammoMultiplier = 2f;
                        collidesGround = false;

                        hitColor = backColor = trailColor = Pal.darkerMetal;
                        frontColor = Pal.darkMetal;
                    }},
                    Items.graphite, new MissileBulletType(1.1f*3f, 60){{
                        width = 22f;
                        height = 22f;
                        shrinkY = 0f;
                        homingPower = 0.18f;
                        homingRange = 600f;
                        lifetime = 300f;
                        reloadMultiplier = 0.6f;
                        buildingDamageMultiplier = 0.2f;
                        pierceDamageFactor = 0.8f;
                        pierceCap = 5;
                        ammoMultiplier = 2f;

                        hitColor = backColor = trailColor = Pal.graphiteAmmoBack;
                        frontColor = Pal.graphiteAmmoFront;
                    }},
                    Items.silicon, new MissileBulletType(3f*3f, 30){{
                        width = 22f;
                        height = 22f;
                        shrinkY = 0f;
                        homingPower = 0.36f;
                        homingRange = 600f;
                        lifetime = 300f;
                        reloadMultiplier = 1.3f;
                        buildingDamageMultiplier = 0.2f;
                        pierceDamageFactor = 0.8f;
                        pierceCap = 5;
                        ammoMultiplier = 5f;

                        hitColor = backColor = trailColor = Pal.siliconAmmoBack;
                        frontColor = Pal.siliconAmmoFront;
                    }},
                    SFWarItems.greenStardust, new MissileBulletType(6f*3f, 18){{
                        width = 6f;
                        height = 6f;
                        shrinkY = 0f;
                        homingPower = 0.36f;
                        homingRange = 600f;
                        lifetime = 300f;
                        reloadMultiplier = 3f;
                        ammoMultiplier = 10f;
                        buildingDamageMultiplier = 0.2f;
                        pierceDamageFactor = 0.8f;
                        pierceCap = 5;
                        ammoMultiplier = 5f;

                        hitColor = backColor = trailColor = Color.valueOf("4bd452");
                        frontColor = Color.valueOf("27e436");
                    }}
            );

            maxAmmo = 10;
            ammoPerShot = 1;
            rotateSpeed = 2f;
            reload = 40f;
            ammoUseEffect = Fx.casing3Double;
            recoil = 1f;
            cooldownTime = reload;
            shake = 4f;
            size = 1;
            unitSort = UnitSorts.weakest;
            envEnabled |= Env.space;
            range = 240f;

            coolantMultiplier = 0.6f;
            liquidCapacity = 6f;
            scaledHealth = 100;

            coolant = consumeCoolant(1f);
            depositCooldown = 2.0f;
            consumesPower = false;
        }};
        comet = new ItemTurret("comet"){{

            requirements(Category.turret, with(Items.titanium, 125, Items.lead, 250, SFWarItems.crystallizedGreenStardust, 150, SFWarItems.blueStardust, 150));
            ammo(
                    Items.titanium, new MissileBulletType(2f*3f, 175){{
                        width = 20f;
                        height = 20f;
                        shrinkY = 0f;
                        homingPower = 0.10f;
                        homingRange = 400f;
                        lifetime = 300f;
                        buildingDamageMultiplier = 0.2f;
                        pierceDamageFactor = 0.8f;
                        pierceCap = 5;
                        ammoMultiplier = 2f;
                        collidesAir = false;

                        hitColor = backColor = trailColor = Color.valueOf("919fe7");
                        frontColor = Color.valueOf("a4b8fa");
                    }},
                    Items.metaglass, new MissileBulletType(2.6f*3f, 150){{
                        width = 20f;
                        height = 20f;
                        shrinkY = 0f;
                        homingPower = 0.14f;
                        homingRange = 500f;
                        lifetime = 300f;
                        buildingDamageMultiplier = 0.2f;
                        pierceDamageFactor = 0.8f;
                        pierceCap = 5;
                        ammoMultiplier = 2f;
                        collidesGround = false;

                        hitColor = backColor = trailColor = Pal.glassAmmoBack;
                        frontColor = Pal.glassAmmoFront;
                    }},
                    Items.graphite, new MissileBulletType(1.1f*3f, 300){{
                        width = 22f;
                        height = 22f;
                        shrinkY = 0f;
                        homingPower = 0.18f;
                        homingRange = 600f;
                        lifetime = 300f;
                        reloadMultiplier = 0.6f;
                        buildingDamageMultiplier = 0.2f;
                        pierceDamageFactor = 0.8f;
                        pierceCap = 5;
                        ammoMultiplier = 2f;

                        hitColor = backColor = trailColor = Pal.graphiteAmmoBack;
                        frontColor = Pal.graphiteAmmoFront;
                    }},
                    Items.silicon, new MissileBulletType(3f*3f, 90){{
                        width = 22f;
                        height = 22f;
                        shrinkY = 0f;
                        homingPower = 0.36f;
                        homingRange = 600f;
                        lifetime = 300f;
                        reloadMultiplier = 1.3f;
                        buildingDamageMultiplier = 0.2f;
                        pierceDamageFactor = 0.8f;
                        pierceCap = 5;
                        ammoMultiplier = 5f;

                        hitColor = backColor = trailColor = Pal.siliconAmmoBack;
                        frontColor = Pal.siliconAmmoFront;
                    }},
                    SFWarItems.blueStardust, new MissileBulletType(6f*3f, 50){{
                        width = 6f;
                        height = 6f;
                        shrinkY = 0f;
                        homingPower = 0.36f;
                        homingRange = 600f;
                        lifetime = 300f;
                        reloadMultiplier = 3f;
                        ammoMultiplier = 10f;
                        buildingDamageMultiplier = 0.2f;
                        pierceDamageFactor = 0.8f;
                        pierceCap = 5;
                        ammoMultiplier = 5f;

                        hitColor = backColor = trailColor = Color.valueOf("4684c7");
                        frontColor = Color.valueOf("3d8fe7");
                    }}
            );

            maxAmmo = 10;
            ammoPerShot = 1;
            rotateSpeed = 2f;
            reload = 60f;
            ammoUseEffect = Fx.casing3Double;
            recoil = 1f;
            cooldownTime = reload;
            shake = 4f;
            size = 2;
            unitSort = UnitSorts.weakest;
            envEnabled |= Env.space;
            range = 300f;

            coolantMultiplier = 0.4f;
            liquidCapacity = 6f;
            scaledHealth = 400;

            coolant = consumeCoolant(1f);
            depositCooldown = 2.0f;
            consumePower(2.5f);
        }};
        moonlight = new TractorBeamTurret("moonlight"){{
            requirements(Category.turret, with(Items.silicon, 160, Items.titanium, 110, SFWarItems.crystallizedPurpleStardust, 50));

            hasPower = true;
            size = 3;
            force = 10;
            scaledForce = 5;
            range = 400f;
            damage = 5;
            scaledHealth = 420;
            rotateSpeed = 24;

            consumePower(3.3f);
        }};
        asteroid = new ItemTurret("asteroid"){{

            requirements(Category.turret, with(SFWarItems.solidifiedNyctar, 325, Items.lead, 650, SFWarItems.crystallizedGreenStardust, 150, SFWarItems.blueStardust, 150));
            ammo(
                    Items.titanium, new MissileBulletType(2f*3f, 350){{
                        width = 34f;
                        height = 34f;
                        shrinkY = 0f;
                        homingPower = 0.10f;
                        homingRange = 400f;
                        lifetime = 300f;
                        splashDamageRadius = 100f;
                        splashDamage = 100f;
                        buildingDamageMultiplier = 0.2f;
                        pierceDamageFactor = 1f;
                        pierceCap = 5;
                        ammoMultiplier = 2f;
                        collidesAir = false;

                        hitColor = backColor = trailColor = Color.valueOf("919fe7");
                        frontColor = Color.valueOf("a4b8fa");
                    }},
                    Items.graphite, new MissileBulletType(1.1f*3f, 600){{
                        width = 36f;
                        height = 36f;
                        shrinkY = 0f;
                        homingPower = 0.18f;
                        homingRange = 600f;
                        lifetime = 300f;
                        splashDamageRadius = 50f;
                        splashDamage = 50f;
                        reloadMultiplier = 0.6f;
                        buildingDamageMultiplier = 0.2f;
                        pierceDamageFactor = 1f;
                        pierceCap = 5;
                        ammoMultiplier = 2f;

                        hitColor = backColor = trailColor = Pal.graphiteAmmoBack;
                        frontColor = Pal.graphiteAmmoFront;
                    }},
                    SFWarItems.nyctoSteel, new MissileBulletType(3f*3f, 1000){{
                        width = 42f;
                        height = 42f;
                        shrinkY = 0f;
                        homingPower = 0.36f;
                        homingRange = 600f;
                        lifetime = 300f;
                        splashDamageRadius = 150f;
                        splashDamage = 25f;
                        reloadMultiplier = 0.8f;
                        ammoMultiplier = 10f;
                        buildingDamageMultiplier = 0.2f;
                        pierceDamageFactor = 1f;
                        pierceCap = 5;
                        ammoMultiplier = 2f;

                        hitColor = backColor = trailColor = Color.valueOf("8e00c2");
                        frontColor = Color.valueOf("ab1ae0");
                    }}
            );

            maxAmmo = 40;
            ammoPerShot = 4;
            rotateSpeed = 2f;
            reload = 50f;
            ammoUseEffect = Fx.casing3Double;
            recoil = 1f;
            cooldownTime = reload;
            shake = 4f;
            size = 3;
            unitSort = UnitSorts.weakest;
            envEnabled |= Env.space;
            range = 360f;

            coolantMultiplier = 0.1f;
            liquidCapacity = 6f;
            scaledHealth = 700;

            coolant = consumeCoolant(1f);
            depositCooldown = 2.0f;
            consumePower(5f);
        }};
        solarEruption = new LaserTurret("solar-eruption"){{
            requirements(Category.turret, with(Items.copper, 1200, Items.lead, 350, Items.graphite, 300, Items.surgeAlloy, 325, Items.silicon, 325));
            shootEffect = Fx.shootBigSmoke2;
            shootCone = 40f;
            recoil = 6f;
            size = 5;
            shake = 2f;
            range = 420;
            reload = 90f;
            firingMoveFract = 0.5f;
            shootDuration = 600f;
            shootSound = Sounds.shootMeltdown;
            loopSound = Sounds.beamMeltdown;
            loopSoundVolume = 2f;
            envEnabled |= Env.space;

            shootType = new ContinuousLaserBulletType(78){{
                length = 440;
                hitEffect = Fx.hitMeltdown;
                hitColor = Pal.meltdownHit;
                status = StatusEffects.melting;
                drawSize = 420f;
                timescaleDamage = true;

                incendChance = 0.4f;
                incendSpread = 5f;
                incendAmount = 1;
                ammoMultiplier = 1f;
            }};

            scaledHealth = 200;
            liquidCapacity = 60f;
            coolant = consumeCoolant(0.5f);
            consumePower(17f);
        }};

        heavenPiercer = new ItemTurret("heaven-piercer"){{
            float brange = range = 1200f;

            requirements(Category.turret, with(Items.silicon, 2500, SFWarItems.solidifiedNyctar, 2500, SFWarItems.nyctoSteel, 2500));
            ammo(
                    SFWarItems.heavenPiercingShell, new SuperRaulCannonBullet(){{
                        shootEffect = SFWarFX.instShootHeaven;
                        hitEffect = SFWarFX.instHitHeaven;
                        pierceEffect = Fx.railHit;
                        smokeEffect = Fx.smokeCloud;
                        pointEffect = SFWarFX.instTrailHeaven;
                        despawnEffect = SFWarFX.instBombHeaven;
                        pointEffectSpace = 30f;
                        damage = 35000;
                        splashDamageRadius = 150f;
                        splashDamage = 5000f;
                        buildingDamageMultiplier = 2f;
                        pierceDamageFactor = 1f;
                        length = brange;
                        hitShake = 3f;
                        ammoMultiplier = 1f;
                    }}
            );

            maxAmmo = 4;
            ammoPerShot = 1;
            rotateSpeed = 10f;
            reload = 180f;
            ammoUseEffect = Fx.casing3Double;
            recoil = 10f;
            cooldownTime = reload;
            shake = 4f;
            size = 6;
            shootCone = 2f;
            shootSound = Sounds.shootForeshadow;
            unitSort = UnitSorts.strongest;
            envEnabled |= Env.space;

            coolantMultiplier = 0.05f;
            liquidCapacity = 60f;
            scaledHealth = 25000f / 36f;

            coolant = consumeCoolant(1f);
            depositCooldown = 2.0f;
            consumePower(100f);
        }};


    }
}
