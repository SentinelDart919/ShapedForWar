package b919.SFWar.content.units;

import arc.struct.Seq;
import mindustry.ai.UnitCommand;
import mindustry.ai.types.MinerAI;
import mindustry.ai.types.RepairAI;
import mindustry.entities.bullet.BulletType;
import mindustry.gen.MechUnit;
import mindustry.gen.Unit;
import mindustry.gen.UnitEntity;
import mindustry.gen.Unitc;
import mindustry.graphics.Pal;
import mindustry.type.UnitType;
import mindustry.type.weapons.RepairBeamWeapon;

import static mindustry.Vars.tilesize;
/* Constructor examples:

flying - UnitEntity::create - normal flying units
mech - MechUnit::create - walker units aka mechs (Dagger, Mace, Nova)
legs - LegsUnit::create - unit with legs (toxopid, Corvus, etc)
naval - UnitWaterMove::create - boat
payload - PayloadUnit::create - payload?
missile - TimedKillUnit::create - literally fucking dies
tank - TankUnit::create - tonk
hover - ElevationMoveUnit::create - ground unit that hover above liquids and other terrains
tether - BuildingTetherPayloadUnit::create - no fucking idea what this is?
crawl - CrawlUnit::create - no is not for crawlers */
public class TerranUnits {
    public static UnitType administrator,
    worker, marine, scout, medic, hellWalker;
    public static void load(){
        administrator = new UnitType("administrator"){{
            coreUnitDock = true;
            //controller = u -> new BuilderAI(true, coreFleeRange); - I commented this out, is how the unit is controlled by AI
            isEnemy = false;
            envDisabled = 0;
            constructor = UnitEntity::create;
            range = 65f;
            faceTarget = true;
            targetPriority = -2;
            lowAltitude = false;
            mineWalls = true;
            mineFloor = false;
            mineHardnessScaling = false;
            flying = true;
            mineSpeed = 9f;
            mineTier = 3;
            buildSpeed = 1.5f;
            drag = 0.08f;
            speed = 7.5f;
            rotateSpeed = 8f;
            accel = 0.08f;
            itemCapacity = 110;
            health = 700f;
            armor = 3f;
            hitSize = 12f;
            buildBeamOffset = 8f;
            payloadCapacity = 2f * 2f * tilesize * tilesize;
            pickupUnits = false;
            vulnerableWithPayloads = true;

            fogRadius = 0f;
            targetable = false;
            hittable = false;

            engineOffset = 7.5f;
            engineSize = 3.4f;

            setEnginesMirror(
                    new UnitEngine(35 / 4f, -13 / 4f, 2.7f, 315f),
                    new UnitEngine(28 / 4f, -35 / 4f, 2.7f, 315f)
            );

            weapons.add(new RepairBeamWeapon(){{
                widthSinMag = 0.11f;
                reload = 20f;
                x = 19f/4f;
                y = 19f/4f;
                rotate = false;
                shootY = 0f;
                beamWidth = 0.7f;
                aimDst = 0f;
                shootCone = 40f;
                mirror = true;

                repairSpeed = 3.6f / 2f;
                fractionRepairSpeed = 0.03f;

                targetUnits = false;
                targetBuildings = true;
                autoTarget = false;
                controllable = true;
                laserColor = Pal.accent;
                healColor = Pal.accent;

                bullet = new BulletType(){{
                    maxRange = 65f;
                }};
            }});
        }};
        worker = new UnitType("worker"){{
            defaultCommand = UnitCommand.mineCommand;
            commands = new Seq<UnitCommand>().add(UnitCommand.repairCommand, UnitCommand.mineCommand, UnitCommand.rebuildCommand, UnitCommand.moveCommand);
            constructor = MechUnit::create;
            canBoost = true;
            boostMultiplier = 1.25f;
            faceTarget = true;
            speed = 0.65f;
            hitSize = 8f;
            health = 120f;
            buildSpeed = 0.4f;
            armor = 1f;
            isEnemy = false;
            mineTier = 1;
            mineSpeed = 2.5f;
            weapons.add(new RepairBeamWeapon("b919-SFWar-mining-weapon"){{
                top = false;
                reload = 20f;
                x = 4.5f;
                alternate = true;
                rotate = true;
                rotationLimit = 90f;
                shootY = 2f;
                beamWidth = 0.7f;

                repairSpeed = 3.6f / 2f;
                fractionRepairSpeed = 0.03f;

                targetUnits = false;
                //targetBuildings = true;
                laserColor = Pal.bulletYellow;
                healColor = Pal.heal;

                bullet = new BulletType(){{
                    maxRange = 95f;
                }};
            }});
        }};
    }
}
