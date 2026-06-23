package b919.SFWar.content.blocks;

import arc.graphics.Color;
import arc.math.Interp;
import arc.math.Mathf;
import arc.struct.Seq;
import mindustry.content.Fx;
import mindustry.content.Items;
import mindustry.content.Liquids;
import mindustry.entities.UnitSorts;
import mindustry.entities.abilities.MoveEffectAbility;
import b919.SFWar.content.bullets.NukeBulletType;
import mindustry.gen.Bullet;
import mindustry.entities.bullet.BulletType;
import mindustry.entities.part.RegionPart;
import mindustry.gen.Sounds;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import mindustry.type.Category;
import mindustry.type.Weapon;
import mindustry.type.unit.MissileUnitType;
import mindustry.world.Block;
import mindustry.world.blocks.defense.turrets.ItemTurret;
import mindustry.world.consumers.ConsumeLiquid;
import mindustry.world.draw.DrawTurret;
import mindustry.world.meta.BuildVisibility;
import mindustry.world.meta.Env;
import mindustry.world.blocks.defense.OverdriveProjector;

import static mindustry.type.ItemStack.with;

public class SFWarDebugBlocks {
    public static Block aliveTree, scathe2, superDuo, scatheNuke, debugTohru;

    public static void load(){
        //aliveTree = new ItsKirby69LivingTreeBlock("alive-tree", 2, "#74d660");

        scathe2 = new ItemTurret("scathe"){{
            requirements(Category.turret, BuildVisibility.shown, with());

            predictTarget = false;

            ammo(
                    Items.carbide, new BulletType(0f, 0f){{
                        shootEffect = Fx.shootBig;
                        smokeEffect = Fx.shootSmokeMissileColor;
                        hitColor = Pal.redLight;
                        ammoMultiplier = 1f;

                        spawnUnit = new MissileUnitType("scathe-missile"){{
                            speed = 4.6f;
                            maxRange = 6f;
                            lifetime = 60f * 5.5f;
                            hitSize = 10f;
                            outlineColor = Pal.darkOutline;
                            engineColor = trailColor = Pal.redLight;
                            engineLayer = Layer.effect;
                            engineSize = 3.1f;
                            engineOffset = 10f;
                            rotateSpeed = 0.25f;
                            trailLength = 18;
                            missileAccelTime = 50f;
                            lowAltitude = true;
                            loopSound = Sounds.loopMissileTrail;
                            loopSoundVolume = 0.6f;
                            deathSound = Sounds.explosionMissile;
                            targetAir = false;
                            targetUnderBlocks = false;

                            fogRadius = 6f;

                            health = 240;

                            weapons.add(new Weapon(){{
                                shootCone = 360f;
                                mirror = false;
                                reload = 1f;
                                deathExplosionEffect = Fx.massiveExplosion;
                                shootOnDeath = true;
                                shake = 10f;
                                bullet = new NukeBulletType(17f, 50000f);
                            }});

                            abilities.add(new MoveEffectAbility(){{
                                effect = Fx.missileTrailSmoke;
                                rotation = 180f;
                                y = -9f;
                                color = Color.grays(0.6f).lerp(Pal.redLight, 0.5f).a(0.4f);
                                interval = 7f;
                            }});
                        }};
                    }}
            );

            drawer = new DrawTurret("reinforced-"){{
                parts.add(new RegionPart("-blade"){{
                              progress = PartProgress.warmup;
                              heatProgress = PartProgress.warmup;
                              heatColor = Color.red;
                              moveRot = -22f;
                              moveX = 0f;
                              moveY = -5f;
                              mirror = true;
                              children.add(new RegionPart("-side"){{
                                  progress = PartProgress.warmup.delay(0.6f);
                                  heatProgress = PartProgress.recoil;
                                  heatColor = Color.red;
                                  mirror = true;
                                  under = false;
                                  moveY = -4f;
                                  moveX = 1f;

                                  moves.add(new PartMove(PartProgress.recoil, 1f, 6f, -40f));
                              }});
                          }},
                        new RegionPart("-mid"){{
                            progress = PartProgress.recoil;
                            heatProgress = PartProgress.warmup.add(-0.2f).add(p -> Mathf.sin(9f, 0.2f) * p.warmup);
                            mirror = false;
                            under = true;
                            moveY = -5f;
                        }}, new RegionPart("-missile"){{
                            progress = PartProgress.reload.curve(Interp.pow2In);

                            colorTo = new Color(1f, 1f, 1f, 0f);
                            color = Color.white;
                            mixColorTo = Pal.accent;
                            mixColor = new Color(1f, 1f, 1f, 0f);
                            outline = false;
                            under = true;

                            layerOffset = -0.01f;

                            moves.add(new PartMove(PartProgress.warmup.inv(), 0f, -4f, 0f));
                        }});
                setAmmoParts(
                        Items.carbide, Seq.with(new RegionPart("-missile"){{
                            progress = PartProgress.reload.curve(Interp.pow2In);

                            colorTo = new Color(1f, 1f, 1f, 0f);
                            color = Color.white;
                            mixColorTo = Pal.accent;
                            mixColor = new Color(1f, 1f, 1f, 0f);
                            outline = false;
                            under = true;
                            layerOffset = -0.01f;

                            moves.add(new PartMove(PartProgress.warmup.inv(), 0f, -4f, 0f));
                        }})
                );
            }};

            recoil = 0.5f;

            fogRadiusMultiplier = 0.4f;
            coolantMultiplier = 15f;
            shootSound = Sounds.shootScathe;

            minWarmup = 0.94f;
            newTargetInterval = 40f;
            unitSort = UnitSorts.strongest;
            shootWarmupSpeed = 0.03f;
            targetAir = false;
            targetUnderBlocks = false;

            shake = 6f;
            ammoPerShot = 15;
            maxAmmo = 45;
            shootY = -1;
            outlineColor = Pal.darkOutline;
            size = 4;
            envEnabled |= Env.space;
            reload = 600f;
            range = 1350;
            shootCone = 1f;
            scaledHealth = 220;
            rotateSpeed = 0.9f;

            coolant = consume(new ConsumeLiquid(Liquids.water, 15f / 60f));
            limitRange();
        }};
        superDuo = new ItemTurret("super-duo"){{
            requirements(Category.turret, BuildVisibility.shown, with());
            reload = 100f;
            range = 1000f;
            shootCone = 0.5f;
            scaledHealth = 1000;
            rotateSpeed = 0.5f;
            coolant = consume(new ConsumeLiquid(Liquids.water, 15f / 60f));
            limitRange();
            ammo(Items.coal, new NukeBulletType(10f, 10000f));
        }};
        scatheNuke = new ItemTurret("scathe-nuke"){{
            requirements(Category.turret, BuildVisibility.shown, with());

            predictTarget = false;

            ammo(
                    Items.carbide, new BulletType(0f, 0f){{
                        shootEffect = Fx.shootBig;
                        smokeEffect = Fx.shootSmokeMissileColor;
                        hitColor = Pal.redLight;
                        ammoMultiplier = 1f;

                        spawnUnit = new MissileUnitType("scathe-nuke-missile"){{
                            speed = 4.6f;
                            maxRange = 6f;
                            lifetime = 60f * 5.5f;
                            hitSize = 10f;
                            outlineColor = Pal.darkOutline;
                            engineColor = trailColor = Pal.redLight;
                            engineLayer = Layer.effect;
                            engineSize = 3.1f;
                            engineOffset = 10f;
                            rotateSpeed = 0.25f;
                            trailLength = 18;
                            missileAccelTime = 50f;
                            lowAltitude = true;
                            loopSound = Sounds.loopMissileTrail;
                            loopSoundVolume = 0.6f;
                            deathSound = Sounds.explosionMissile;
                            targetAir = false;
                            targetUnderBlocks = false;

                            fogRadius = 6f;
                            health = 240;

                            weapons.add(new Weapon(){{
                                shootCone = 360f;
                                mirror = false;
                                reload = 1f;
                                deathExplosionEffect = Fx.massiveExplosion;
                                shootOnDeath = true;
                                shake = 10f;
                                bullet = new BulletType(0f, 0f){
                                    {
                                        lifetime = 1f;
                                        despawnHit = true;
                                        hitEffect = Fx.none;
                                        despawnEffect = Fx.none;
                                    }

                                    public void hit(Bullet b, float x, float y){
                                        super.hit(b, x, y);
                                        NukeBulletType.createNukeExplosion(b.x, b.y, b.team, b.rotation());
                                    }
                                };
                            }});
                        }};
                    }}
            );

            drawer = new DrawTurret("reinforced-"){{
                parts.add(new RegionPart("-blade"){{
                              progress = PartProgress.warmup;
                              heatProgress = PartProgress.warmup;
                              heatColor = Color.red;
                              moveRot = -22f;
                              moveX = 0f;
                              moveY = -5f;
                              mirror = true;
                              children.add(new RegionPart("-side"){{
                                  progress = PartProgress.warmup.delay(0.6f);
                                  heatProgress = PartProgress.recoil;
                                  heatColor = Color.red;
                                  mirror = true;
                                  under = false;
                                  moveY = -4f;
                                  moveX = 1f;

                                  moves.add(new PartMove(PartProgress.recoil, 1f, 6f, -40f));
                              }});
                          }},
                        new RegionPart("-mid"){{
                            progress = PartProgress.recoil;
                            heatProgress = PartProgress.warmup.add(-0.2f).add(p -> Mathf.sin(9f, 0.2f) * p.warmup);
                            mirror = false;
                            under = true;
                            moveY = -5f;
                        }}, new RegionPart("-missile"){{
                            progress = PartProgress.reload.curve(Interp.pow2In);

                            colorTo = new Color(1f, 1f, 1f, 0f);
                            color = Color.white;
                            mixColorTo = Pal.accent;
                            mixColor = new Color(1f, 1f, 1f, 0f);
                            outline = false;
                            under = true;

                            layerOffset = -0.01f;

                            moves.add(new PartMove(PartProgress.warmup.inv(), 0f, -4f, 0f));
                        }});
                setAmmoParts(
                        Items.carbide, Seq.with(new RegionPart("-missile"){{
                            progress = PartProgress.reload.curve(Interp.pow2In);

                            colorTo = new Color(1f, 1f, 1f, 0f);
                            color = Color.white;
                            mixColorTo = Pal.accent;
                            mixColor = new Color(1f, 1f, 1f, 0f);
                            outline = false;
                            under = true;
                            layerOffset = -0.01f;

                            moves.add(new PartMove(PartProgress.warmup.inv(), 0f, -4f, 0f));
                        }})
                );
            }};

            recoil = 0.5f;

            fogRadiusMultiplier = 0.4f;
            coolantMultiplier = 15f;
            shootSound = Sounds.shootScathe;

            minWarmup = 0.94f;
            newTargetInterval = 40f;
            unitSort = UnitSorts.strongest;
            shootWarmupSpeed = 0.03f;
            targetAir = false;
            targetUnderBlocks = false;

            shake = 6f;
            ammoPerShot = 15;
            maxAmmo = 45;
            shootY = -1;
            outlineColor = Pal.darkOutline;
            size = 4;
            envEnabled |= Env.space;
            reload = 600f;
            range = 1350;
            shootCone = 1f;
            scaledHealth = 220;
            rotateSpeed = 0.9f;

            coolant = consume(new ConsumeLiquid(Liquids.water, 15f / 60f));
            limitRange();
        }};
        debugTohru = new OverdriveProjector("debug-tohru"){{
            requirements(Category.effect, BuildVisibility.sandboxOnly, with());
            consumePower(0.50f);
            size = 4;
            range = 300f;
            speedBoost = 6f;
            useTime = 300f;
            ambientSoundVolume = 0.16f;
            hasBoost = false;
        }};
    }
}
