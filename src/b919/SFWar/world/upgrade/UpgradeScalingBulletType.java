package b919.SFWar.world.upgrade;

import arc.struct.Seq;
import mindustry.entities.bullet.BasicBulletType;
import mindustry.gen.Bullet;
/** Bullet that can get damage scaling from upgrades.*/ // something tells me this class can be redundant
public class UpgradeScalingBulletType extends BasicBulletType {
    public Upgrade damageUpgrade;
    public float baseDamage;

    public UpgradeScalingBulletType(float speed, float damage, String bulletSprite) {
        super(speed, damage, bulletSprite);
        this.baseDamage = damage;
    }

    public UpgradeScalingBulletType(float speed, float damage) {
        super(speed, damage);
        this.baseDamage = damage;
    }

    @Override
    public float damageMultiplier(Bullet bullet) {
        float mult = 1f;
        if(damageUpgrade != null) {
            mult = UpgradeManager.getFloat(damageUpgrade, 1f);
        }
        return mult;
    }
}
