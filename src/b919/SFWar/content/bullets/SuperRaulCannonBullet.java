package b919.SFWar.content.bullets;

import arc.graphics.Color;
import arc.math.Mathf;
import mindustry.entities.bullet.RailBulletType;
import mindustry.gen.Bullet;
import mindustry.graphics.Pal;

public class SuperRaulCannonBullet extends RailBulletType {

    @Override
    public void init(Bullet b) {
        float range = targetRange(b);
        float orig = length;
        length = range;
        super.init(b);
        length = orig;
    }

    @Override
    public void despawned(Bullet b) {
        float range = targetRange(b);
        super.despawned(b);
        float endX = b.x + Mathf.cosDeg(b.rotation()) * range;
        float endY = b.y + Mathf.sinDeg(b.rotation()) * range;
        NukeBulletType.createNukeExplosion(endX, endY, b.team, b.rotation(),480f,53f, Color.valueOf("ff0000"), Color.valueOf("ff3c3c"));
    }

    private float targetRange(Bullet b) {
        float dx = b.aimX - b.x, dy = b.aimY - b.y;
        float dst = Mathf.sqrt(dx * dx + dy * dy);
        return dst > 0 ? Math.min(dst, b.type.range) : b.type.range;
    }
}
