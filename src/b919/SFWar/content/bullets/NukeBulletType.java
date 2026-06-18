package b919.SFWar.content.bullets;

import arc.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.Tmp;
import b919.SFWar.utils.SFWarFX;
import b919.SFWar.utils.SFWarSFX;
import b919.SFWar.utils.SFWarSounds;
import b919.SFWar.utils.SFWarPal;
import b919.SFWar.utils.SFWarUtils;
import mindustry.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.entities.bullet.*;
import mindustry.game.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.blocks.defense.*;

public class NukeBulletType extends BasicBulletType{
    public NukeBulletType(float speed, float damage){
        super(speed, damage, "missile-large");
        backColor = trailColor = hitColor = SFWarPal.red;
        frontColor = SFWarPal.red.cpy().mul(2f);

        shrinkY = 0f;
        width = 15f;
        height = 34f;

        trailLength = 5;
        trailWidth = 5f;

        lifetime = 60f;

        despawnHit = true;
        collidesTiles = true;

        hitEffect = Fx.massiveExplosion;
        despawnEffect = Fx.none;

        shootEffect = SFWarFX.desNukeShoot;
        smokeEffect = Fx.none;

        ammoMultiplier = 1f;
        fragLifeMin = 0.1f;
        fragBullets = 7;
        fragBullet = new ArtilleryBulletType(3.4f, 32){{
            buildingDamageMultiplier = 0.1f;
            drag = 0.02f;
            hitEffect = Fx.massiveExplosion;
            despawnEffect = Fx.scatheSlash;
            knockback = 0.8f;
            lifetime = 23f;
            width = height = 18f;
            collidesTiles = false;
            splashDamageRadius = 40f;
            splashDamage = 100f;
            backColor = trailColor = hitColor = Pal.redLight;
            frontColor = Color.white;
            smokeEffect = Fx.shootBigSmoke2;
            despawnShake = 7f;
            lightRadius = 30f;
            lightColor = Pal.redLight;
            lightOpacity = 0.5f;

            trailLength = 20;
            trailWidth = 3.5f;
            trailEffect = Fx.none;
        }};
    }

    @Override
    public void hit(Bullet b, float x, float y){
        super.hit(b, x, y);
        createNukeExplosion(b.x, b.y, b.team, b.rotation());
    }

    @Override
    public void hitTile(Bullet b, Building build, float x, float y, float initialHealth, boolean direct){
        super.hitTile(b, build, x, y, initialHealth, direct);
        createNukeExplosion(b.x, b.y, b.team, b.rotation());
    }
    @Override
    public void hitEntity(Bullet b, Hitboxc entity, float health){
        super.hitEntity(b, entity, health);
        createNukeExplosion(b.x, b.y, b.team, b.rotation());
    }
    @Override
    public void despawned(Bullet b){
        super.despawned(b);
        createNukeExplosion(b.x, b.y, b.team, b.rotation());
    }

    public static void createNukeExplosion(float x, float y, Team team, float rotation){

        if(SFWarSounds.desNukeHit != null){
            int sid1 = SFWarSounds.desNukeHit.at(x, y, 1f, 2f);
            Core.audio.protect(sid1, true);
            float fall = Mathf.pow(Mathf.clamp(1f - SFWarSounds.desNukeHit.calcFalloff(x, y) * 1.1f), 1.5f);
            int sid2 = SFWarSounds.desNukeHitFar.play(fall * 2f, 1f, SFWarSounds.desNukeHit.calcPan(x, y));
            Core.audio.protect(sid2, true);
        }

        float[] arr = new float[360 * 3];
        SFWarUtils.rayCastCircle(x, y, 480f, t -> (t.block().isStatic() || t.block() instanceof Wall) && !Mathf.within(x, y, t.worldx(), t.worldy(), 150f), t -> {
            float dst = 1f - Mathf.clamp(Mathf.dst(x, y, t.x * Vars.tilesize, t.y * Vars.tilesize) / 480f);
            if(Mathf.chance(Mathf.pow(dst, 2f) * 0.75f)) Fires.create(t);
        }, t -> {
            float nx = t.x * Vars.tilesize, ny = t.y * Vars.tilesize;
            float ang = Angles.angle(x, y, nx, ny);

            SFWarFX.desNukeShockSmoke.at(nx, ny, ang);
        }, bl -> {
            float d = 21000f + bl.maxHealth / 5f;
            bl.damage(d);
            if(bl.dead()){
                bl.block.destroySound.at(bl);
                Fx.dynamicExplosion.at(bl.x, bl.y, (Vars.tilesize * bl.block.size) / 2f / 8f);
                float shake = bl.hitSize() / 3f;
                Effect.shake(shake, shake, bl);
                if(bl.block.createRubble && !bl.floor().solid && !bl.floor().isLiquid){
                    Effect.rubble(bl.x, bl.y, bl.block.size);
                }
            }
        }, arr);

        SFWarUtils.scanEnemies(team, x, y, 480f, true, true, t -> {
            if(t instanceof Unit u){
                float damageScl = SFWarUtils.inRayCastCircle(x, y, arr, u);

                if(damageScl > 0){
                    Tmp.v2.trns(Angles.angle(x, y, u.x, u.y), (16f + 5f / u.mass()) * damageScl);
                    u.vel.add(Tmp.v2);

                    float ud = (u.maxHealth / 10f + 10000f) * damageScl;
                    u.damage(ud);

                    SFWarFX.desNukeVaporize.at(u.x, u.y, u.angleTo(x, y) + 180f, u.hitSize / 2f);
                }
            }else if(t instanceof Building bl){
                float damageScl = SFWarUtils.inRayCastCircle(x, y, arr, bl);
                if(damageScl > 0){
                    if(t.within(x, y, 150f + bl.hitSize() / 2f)){
                        bl.damage((bl.maxHealth / 10f + 10000f) * damageScl);

                        SFWarFX.desNukeVaporize.at(bl.x, bl.y, bl.angleTo(x, y) + 180f, bl.hitSize() / 2f);
                    }else{
                        float bd = (bl.maxHealth / 10f + 10000f) * damageScl;
                        bl.damage(bd);
                        if(bl.dead()){
                            bl.block.destroySound.at(bl);
                            Fx.dynamicExplosion.at(bl.x, bl.y, (Vars.tilesize * bl.block.size) / 2f / 8f);
                            float shake = bl.hitSize() / 3f;
                            Effect.shake(shake, shake, bl);
                            if(bl.block.createRubble && !bl.floor().solid && !bl.floor().isLiquid){
                                Effect.rubble(bl.x, bl.y, bl.block.size);
                            }
                        }else{
                            Fx.massiveExplosion.at(bl.x, bl.y, bl.hitSize() / 2f);
                        }
                    }
                }
            }
        });

        Effect.shake(60f, 120f, x, y);
        SFWarFX.desNukeShockwave.at(x, y, 480f);
        SFWarFX.desNuke.at(x, y, 479f, arr);

        if(SFWarSFX.inst != null){
            SFWarSFX.inst.impactFrames(x, y, rotation, 53f, false, () -> {
                for(int i = 0; i < arr.length; i++){
                    float len1 = arr[i], len2 = arr[(i + 1) % arr.length];
                    float ang1 = (i / (float)arr.length) * 360f;
                    float ang2 = ((i + 1f) / arr.length) * 360f;

                    float x1 = Mathf.cosDeg(ang1) * len1, y1 = Mathf.sinDeg(ang1) * len1;
                    float x2 = Mathf.cosDeg(ang2) * len2, y2 = Mathf.sinDeg(ang2) * len2;

                    Fill.tri(x, y, x + x1, y + y1, x + x2, y + y2);
                }
            });
        }

        Draw.draw(Layer.end - 1, () -> {
            for(int i = 0; i < arr.length; i++){
                float len1 = arr[i], len2 = arr[(i + 1) % arr.length];
                float ang1 = (i / (float)arr.length) * 360f;
                float ang2 = ((i + 1f) / arr.length) * 360f;

                float x1 = Mathf.cosDeg(ang1) * len1, y1 = Mathf.sinDeg(ang1) * len1;
                float x2 = Mathf.cosDeg(ang2) * len2, y2 = Mathf.sinDeg(ang2) * len2;

                Fill.tri(x, y, x + x1, y + y1, x + x2, y + y2);
            }
        });
    }
}
