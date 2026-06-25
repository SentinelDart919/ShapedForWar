package b919.SFWar.content.blocks;

import arc.graphics.Color;
import b919.SFWar.content.HeavenPiercerFx;
import b919.SFWar.world.terran.nebulae.blocks.power.NebulaePanel;
import mindustry.content.*;
import mindustry.entities.UnitSorts;
import mindustry.entities.bullet.*;
import mindustry.entities.pattern.ShootBarrel;
import mindustry.gen.Sounds;
import mindustry.graphics.Pal;
import mindustry.type.Category;
import mindustry.world.Block;
import mindustry.world.blocks.defense.ForceProjector;
import mindustry.world.blocks.defense.turrets.ItemTurret;
import mindustry.world.blocks.production.GenericCrafter;
import mindustry.world.blocks.storage.CoreBlock;
import mindustry.world.meta.BuildVisibility;
import mindustry.type.*; // this will import the classes in this package but not the classes in subpackages
import mindustry.type.unit.*; // this is not for units this is to set entities, not units
import b919.SFWar.content.units.NebulaeUnits;
import b919.SFWar.content.CustomFx;
import b919.SFWar.content.SFWarItems;
import b919.SFWar.content.SFWarLiquids;
import mindustry.world.blocks.power.*;
import mindustry.world.meta.Env;

import static mindustry.type.ItemStack.with;

public class NebulaeBlocks {
    public static Block
            // cores
        domusLucis, castellumLucis, arxLucis,
            // production
        greenStardustPlant, blueStardustFormationPlant, gaseousNyctarPlant,
            // defensive
        luminosityCondenser,
            // turrets
        crescentMoon, heavenPiercer, starScreech, pathOfTotality, meteoroid, comet, asteroid,
            // units
        seminarumStellarum, sartrixNyctar,
            // power
        nyxPanel, nyxPanelSmall;
    public static void load(){

        domusLucis = new CoreBlock("domus-lucis"){{
            requirements(Category.effect, BuildVisibility.shown, with(SFWarItems.solidifiedNyctar, 1000));
            health = 10800;
            itemCapacity = 17000;
            size = 6;

            unitCapModifier = 32;
            unitType = UnitTypes.toxopid;
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
        greenStardustPlant = new GenericCrafter("green-stardust-plant"){{
            requirements(Category.crafting, with());
            outputItem = new ItemStack(SFWarItems.greenStardust, 1);
            craftTime = 30f;
            size = 2;
            hasPower = true;
            hasLiquids = false;

            consumeItems(with());
            consumePower(0.5f);
        }};
        blueStardustFormationPlant = new GenericCrafter("blue-stardust-formation-plant"){{
            requirements(Category.crafting, with());
            outputItem = new ItemStack(SFWarItems.blueStardust, 1);
            craftTime = 60f;
            liquidCapacity = 60f;
            size = 2;
            hasPower = hasLiquids;
            hasLiquids = true;

            consumeItems(with());
            consumePower(1f);
            consumeLiquid(Liquids.water, 0.1f);
        }};
        gaseousNyctarPlant = new GenericCrafter("gaseous-nyctar-plant"){{
            requirements(Category.crafting, with());
            outputLiquid = new LiquidStack(SFWarLiquids.gaseousNyctar, 6f / 60f);
            craftTime = 60f;
            liquidCapacity = 60f;
            size = 2;
            hasPower = true;
            hasLiquids = true;

            consumeItems(with(Items.coal, 1, SFWarItems.greenStardust, 1));
            consumePower(0.016f);
        }};
        crescentMoon = new ItemTurret("crescent-moon"){{
            float brange = range = 300f;

            requirements(Category.turret, with(Items.silicon, 250, SFWarItems.solidifiedNyctar, 100));
            ammo(
                    SFWarItems.crystallizedBlueStardust, new RailBulletType(){{
                        trailColor = Color.valueOf("4684c7");
                        shootEffect = CustomFx.instShootCrescent;
                        hitEffect = CustomFx.instHitCrescent;
                        pierceEffect = Fx.railHit;
                        smokeEffect = Fx.smokeCloud;
                        pointEffect = CustomFx.instTrailCrescent;
                        despawnEffect = CustomFx.instBombCrescent;
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
                        -8, -2.5f, 0,
                        -4, -1.25f, 0,
                        0, 0, 0,
                        4, -1.25f, 0,
                        8, -2.5f, 0
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
                    Items.copper, new MissileBulletType(2f, 25){{
                        width = 20f;
                        height = 20f;
                        shrinkY = 0f;
                        homingPower = 0.10f;
                        homingRange = 400f;
                        lifetime = 300f;
                        buildingDamageMultiplier = 0.2f;
                        pierceDamageFactor = 0.8f;
                        pierceCap = 5;
                        ammoMultiplier = 1f;
                        collidesAir = false;

                        hitColor = backColor = trailColor = Pal.copperAmmoBack;
                        frontColor = Pal.copperAmmoFront;
                    }},
                    Items.lead, new MissileBulletType(2.6f, 25){{
                        width = 20f;
                        height = 20f;
                        shrinkY = 0f;
                        homingPower = 0.14f;
                        homingRange = 500f;
                        lifetime = 300f;
                        buildingDamageMultiplier = 0.2f;
                        pierceDamageFactor = 0.8f;
                        pierceCap = 5;
                        ammoMultiplier = 1f;
                        collidesGround = false;

                        hitColor = backColor = trailColor = Pal.darkerMetal;
                        frontColor = Pal.darkMetal;
                    }},
                    Items.graphite, new MissileBulletType(1f, 60){{
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
                        ammoMultiplier = 1f;

                        hitColor = backColor = trailColor = Pal.graphiteAmmoBack;
                        frontColor = Pal.graphiteAmmoFront;
                    }},
                    Items.silicon, new MissileBulletType(3f, 30){{
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
                    SFWarItems.greenStardust, new MissileBulletType(6f, 18){{
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
            consumePower(0.5f);
        }};
        comet = new ItemTurret("comet"){{

            requirements(Category.turret, with(Items.titanium, 125, Items.lead, 250, SFWarItems.crystallizedGreenStardust, 150, SFWarItems.blueStardust, 150));
            ammo(
                    Items.titanium, new MissileBulletType(2f, 175){{
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
                    Items.metaglass, new MissileBulletType(2.6f, 150){{
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
                    Items.graphite, new MissileBulletType(1f, 300){{
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
                        ammoMultiplier = 1f;

                        hitColor = backColor = trailColor = Pal.graphiteAmmoBack;
                        frontColor = Pal.graphiteAmmoFront;
                    }},
                    Items.silicon, new MissileBulletType(3f, 90){{
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
                    SFWarItems.blueStardust, new MissileBulletType(6f, 50){{
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
        asteroid = new ItemTurret("asteroid"){{

            requirements(Category.turret, with(SFWarItems.solidifiedNyctar, 325, Items.lead, 650, SFWarItems.crystallizedGreenStardust, 150, SFWarItems.blueStardust, 150));
            ammo(
                    Items.titanium, new MissileBulletType(2f, 350){{
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
                        ammoMultiplier = 1f;
                        collidesAir = false;

                        hitColor = backColor = trailColor = Color.valueOf("919fe7");
                        frontColor = Color.valueOf("a4b8fa");
                    }},
                    Items.graphite, new MissileBulletType(1f, 600){{
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
                        ammoMultiplier = 1f;

                        hitColor = backColor = trailColor = Pal.graphiteAmmoBack;
                        frontColor = Pal.graphiteAmmoFront;
                    }},
                    SFWarItems.nyctoSteel, new MissileBulletType(3f, 1000){{
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
                        ammoMultiplier = 1f;

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
            range = 300f;

            coolantMultiplier = 0.1f;
            liquidCapacity = 6f;
            scaledHealth = 700;

            coolant = consumeCoolant(1f);
            depositCooldown = 2.0f;
            consumePower(5f);
        }};
        heavenPiercer = new ItemTurret("heaven-piercer"){{
            float brange = range = 1200f;

            requirements(Category.turret, with(Items.silicon, 2500, SFWarItems.solidifiedNyctar, 2500, SFWarItems.nyctoSteel, 2500));
            ammo(
                    SFWarItems.nyctoSteel, new RailBulletType(){{
                        shootEffect = HeavenPiercerFx.instShootHeaven;
                        hitEffect = HeavenPiercerFx.instHitHeaven;
                        pierceEffect = Fx.railHit;
                        smokeEffect = Fx.smokeCloud;
                        pointEffect = HeavenPiercerFx.instTrailHeaven;
                        despawnEffect = HeavenPiercerFx.instBombHeaven;
                        pointEffectSpace = 30f;
                        damage = 35000;
                        splashDamageRadius = 150f;
                        splashDamage = 5000f;
                        buildingDamageMultiplier = 0.2f;
                        pierceDamageFactor = 1f;
                        length = brange;
                        hitShake = 3f;
                        ammoMultiplier = 1f;
                    }}
            );

            maxAmmo = 1000;
            ammoPerShot = 250;
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
