package b919.SFWar.content.units;

import b919.SFWar.content.SFWarUpgrades;
import b919.SFWar.world.upgrade.UpgradeScalingBulletType;
import mindustry.content.Fx;
import mindustry.gen.MechUnit;
import mindustry.type.UnitType;
import mindustry.type.Weapon;
public class DebugUnit {
    public static UnitType debugDagger;

    public static void load(){
        debugDagger = new UnitType("debug-dagger"){{
            constructor = MechUnit::create;
            speed = 0.5f;
            hitSize = 8f;
            health = 180f;
            armor = 1f;
            weapons.add(new Weapon(){{
                bullet = new UpgradeScalingBulletType(5f, 12f){{
                    damageUpgrade = SFWarUpgrades.damageUpgrade;
                    shootEffect = Fx.shootSmall;
                    smokeEffect = Fx.shootSmallSmoke;
                    hitEffect = Fx.hitBulletSmall;
                    despawnEffect = Fx.hitBulletSmall;
                }};
                reload = 24f;
                x = 4.25f;
                y = 1f;
            }});
        }};
    }
}
