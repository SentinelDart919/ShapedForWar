package b919.SFWar.content.blocks;

import arc.graphics.Color;
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
        crescentMoon, heavenPiercer, starScreech, pathOfTotality,
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
                        shootEffect = Fx.instShoot;
                        hitEffect = Fx.instHit;
                        pierceEffect = Fx.railHit;
                        smokeEffect = Fx.smokeCloud;
                        pointEffect = Fx.instTrail;
                        despawnEffect = Fx.instBomb;
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
                    SFWarItems.solidifiedNyctar, new MissileBulletType(4.2f, 42){{
                        width = 12f;
                        height = 12f;
                        shrinkY = 0f;
                        homingPower = 0.25f;
                        damage = 25;
                        splashDamageRadius = 35f;
                        splashDamage = 85f * 1.4f;
                        hitEffect = Fx.blastExplosion;
                        despawnEffect = Fx.blastExplosion;
                        ammoMultiplier = 4f;
                        status = StatusEffects.tarred;

                        hitColor = backColor = trailColor = Color.valueOf("ab1ae0");
                        frontColor = Color.valueOf("8e00c2");
                    }},
                    SFWarItems.nyctoSteel, new MissileBulletType(4.2f, 42){{
                        width = 8f;
                        height = 8f;
                        shrinkY = 0f;
                        homingPower = 0.10f;
                        damage = 125;
                        splashDamageRadius = 35f;
                        splashDamage = 10f * 1.4f;
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
                        8, -25.f, 0
                };
                shots = 5;
                shotDelay = 5f;
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

            limitRange(5f);
            coolant = consumeCoolant(0.3f);
            depositCooldown = 2.0f;
        }};


    }
}
