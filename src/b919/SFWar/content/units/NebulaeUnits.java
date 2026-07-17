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
import mindustry.type.weapons.*;

import static mindustry.Vars.tilesize;

public class NebulaeUnits {

    public static UnitType
    blackWasp, serpensMundi;
    public static void load(){
        blackWasp = new UnitType("black-wasp"){{
            coreUnitDock = true;
            //controller = u -> new BuilderAI(true, coreFleeRange); - I commented this out, is how the unit is controlled by AI
            isEnemy = false;
            envDisabled = 0;
            constructor = UnitEntity::create;
            range = 130f;
            faceTarget = false;
            targetPriority = -2;
            lowAltitude = false;
            mineWalls = true;
            mineFloor = false;
            mineHardnessScaling = false;
            flying = true;
            mineSpeed = 10f;
            mineTier = 6;
            buildSpeed = 5f;
            drag = 0.00f;
            speed = 7f;
            rotateSpeed = 8f;
            accel = 0.08f;
            itemCapacity = 180;
            health = 1500f;
            armor = 10f;
            hitSize = 12f;
            buildBeamOffset = 8f;
            payloadCapacity = 3f * 3f * tilesize * tilesize;
            pickupUnits = false;
            vulnerableWithPayloads = true;

            fogRadius = 0f;
            targetable = false;
            hittable = false;


            weapons.add(new RepairBeamWeapon(){{
                widthSinMag = 0.31f;
                reload = 20f;
                x = 0f;
                y = 0f;
                rotate = false;
                shootY = 0f;
                beamWidth = 0.7f;
                aimDst = 0f;
                shootCone = 40f;
                mirror = false;

                repairSpeed = 4.2f;
                fractionRepairSpeed = 0.03f;

                targetUnits = false;
                targetBuildings = true;
                autoTarget = false;
                controllable = true;
                laserColor = Pal.accent;
                healColor = Pal.accent;

                bullet = new BulletType(){{
                    maxRange = 130;
                }};
            }});
        }};
    }
}
