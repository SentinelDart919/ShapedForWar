package b919.SFWar.utils;

import arc.*;
import arc.func.*;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.struct.*;
import arc.util.*;
import mindustry.*;
import mindustry.entities.pattern.*;
import mindustry.entities.bullet.*;
import mindustry.game.EventType;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;

import static mindustry.Vars.*;
//BTW this is not mine is an adaptation of the SFX From FlameOut All Credits to EyeOfDarkness original link:
//https://github.com/EyeOfDarkness/FlameOut/blob/main/src/flame/FlameOutSFX.java
public class SFWarSFX implements ApplicationListener{
    public static SFWarSFX inst;

    private static final Seq<HealPrevention<?>> hpSeq = new Seq<>();
    private static final IntMap<HealPrevention<?>> hpMap = new IntMap<>();
    private static final SimplePool<UnitHealPrevention> uhpPool = new SimplePool<>(UnitHealPrevention::new);
    private static final SimplePool<BuildingHealPrevention> bhpPool = new SimplePool<>(BuildingHealPrevention::new);

    private static float[] bulletDps, unitDps;

    float impFrameTime = 0f;
    Seq<ImpactFrameDrawer> impFrameEntities = new Seq<>();
    SimplePool<ImpactFrameDrawer> impFramePool = new SimplePool<>(ImpactFrameDrawer::new);

    private final SimplePool<LockMovement> lockPool = new SimplePool<>(LockMovement::new);
    private final Seq<LockMovement> locks = new Seq<>();
    private final IntMap<LockMovement> lockMap = new IntMap<>();

    public SFWarSFX(){
        if(Vars.platform instanceof ApplicationCore core){
            core.add(this);
        }
        Events.on(EventType.ResetEvent.class, e -> {
            locks.clear();
            lockMap.clear();
        });
        Events.on(EventType.ContentInitEvent.class, e -> Core.app.post(() -> {
            Seq<BulletType> bullets = content.bullets();
            Seq<UnitType> units = content.units();
            bulletDps = new float[bullets.size];
            unitDps = new float[units.size];
            for(BulletType b : bullets){
                updateBullet(b);
            }
            for(UnitType u : units){
                updateUnit(u);
            }
        }));
        Events.run(EventType.Trigger.draw, this::draw);
        inst = this;
    }

    public float getUnitDps(UnitType unit){
        if(unit.id >= unitDps.length) return 0f;
        return unitDps[unit.id];
    }
    public float getBulletDps(BulletType bullet){
        if(bullet.id >= bulletDps.length) return 0f;
        return bulletDps[bullet.id];
    }

    float updateUnit(UnitType unit){
        if(unitDps[unit.id] == 0f){
            unitDps[unit.id] = 0.000001f;
            float damage = 0f;
            for(Weapon w : unit.weapons){
                ShootPattern p = w.shoot;
                float d;
                if(!w.shootOnDeath && !w.bullet.killShooter){
                    d = (updateBullet(w.bullet) * p.shots * (w.continuous ? w.bullet.lifetime / 5f : 1f)) / w.reload;
                }else{
                    d = updateBullet(w.bullet) * p.shots;
                }
                damage += d + (Mathf.pow(unit.hitSize, 0.75f) * unit.crashDamageMultiplier);
            }
            unitDps[unit.id] = damage;
        }
        return unitDps[unit.id];
    }
    float updateBullet(BulletType type){
        if(bulletDps[type.id] == 0f){
            bulletDps[type.id] = type.damage;
            float damage = type.damage + type.splashDamage;

            if(type.fragBullet != null) damage += type.fragBullets * updateBullet(type.fragBullet);
            if(type.lightning > 0){
                damage += type.lightning * Mathf.pow(type.lightningLength, 0.75f) * Math.max(type.lightningType != null ? updateBullet(type.lightningType) : 0f, type.lightningDamage);
            }
            if(type.intervalBullet != null){
                damage += (updateBullet(type.intervalBullet) * type.intervalBullets) / type.bulletInterval;
            }
            if(type.spawnUnit != null){
                damage += updateUnit(type.spawnUnit);
            }
            if(type.despawnUnit != null){
                damage += updateUnit(type.despawnUnit) * type.despawnUnitCount;
            }
            bulletDps[type.id] = Math.max(0.00001f, damage);
        }
        return bulletDps[type.id];
    }

    void draw(){
        if(impFrameTime > 0f && !impFrameEntities.isEmpty()){
            Draw.draw(Layer.effect, () -> {
                for(ImpactFrameDrawer impact : impFrameEntities){
                    float x = impact.x;
                    float y = impact.y;
                    float intensity = impact.intensity;

                    Draw.color(impact.directional ? Color.white : Pal.lightOrange, Color.gray, 0.5f);
                    int ints = (int)(intensity) + 8;
                    for(int i = 0; i < ints; i++){
                        float angle = impact.directional ? impact.rotation + (Interp.pow2In.apply(Mathf.random(1f)) * 180f * Mathf.randomSign()) : Mathf.random(360f);
                        float adst = impact.directional ? (Interp.pow2In.apply((180f - Angles.angleDist(impact.rotation, angle)) / 180f) * 0.75f + 0.25f) : 1f;
                        float range = intensity * 110f * Mathf.random(0.5f, 1f) * adst;
                        float width = intensity * 2f * Mathf.random(0.5f, 1f);

                        Drawf.tri(x, y, width, range, angle);
                        Drawf.tri(x, y, width, width * 2f, angle + 180f);
                    }

                    if(impact.run != null) impact.run.run();
                }
                Draw.color();
            });
        }
    }

    public void impactFrames(float x, float y, float rotation, float intensity, boolean directional, Runnable run){
        ImpactFrameDrawer i = impFramePool.obtain();
        i.run = run;
        i.intensity = intensity;
        i.x = x;
        i.y = y;
        i.rotation = rotation;
        i.directional = directional;

        impFrameEntities.add(i);
        impFrameTime = Math.max(impFrameTime, 6f);
    }
    public void impactFrames(Drawc d, float x, float y, float rotation, float intensity, boolean directional){
        ImpactFrameDrawer i = impFramePool.obtain();
        i.draw = d;
        i.intensity = intensity;
        i.x = x;
        i.y = y;
        i.rotation = rotation;
        i.directional = directional;

        impFrameEntities.add(i);
        impFrameTime = Math.max(impFrameTime, 6f);
    }

    public void cancelMovementLock(Unit unit){
        LockMovement l = lockMap.get(unit.id);
        if(l != null){
            locks.remove(l);
            lockMap.remove(unit.id);
            lockPool.free(l);
        }
    }

    public void addMovementLock(Unit unit, float duration){
        LockMovement l = lockMap.get(unit.id);
        if(l == null){
            l = lockPool.obtain();
            l.u = unit;
            l.id = unit.id;
            l.x = unit.x;
            l.y = unit.y;
            l.time = duration;
            locks.add(l);
            lockMap.put(unit.id, l);
        }else{
            l.time = Math.max(l.time, duration);
        }
    }

    public void lockHealing(Healthc h, float health, float duration){
        HealPrevention<?> a = hpMap.get(h.id());
        if(a != null){
            a.update(duration, health);
        }else{
            if(h instanceof Unit u){
                UnitHealPrevention uh = uhpPool.obtain();
                uh.set(u, duration, health);
                hpMap.put(h.id(), uh);
                hpSeq.add(uh);
            }else if(h instanceof Building b){
                BuildingHealPrevention bh = bhpPool.obtain();
                bh.set(b, duration, health);
                hpMap.put(h.id(), bh);
                hpSeq.add(bh);
            }
        }
    }

    @Override
    public void update(){
        if(Vars.state.isPaused()) return;

        locks.removeAll(l -> {
            l.u.x = Mathf.lerpDelta(l.u.x, l.x, 0.9f);
            l.u.y = Mathf.lerpDelta(l.u.y, l.y, 0.9f);

            l.time -= Time.delta;
            if(l.time <= 0f){
                lockMap.remove(l.id);
                lockPool.free(l);
            }
            return l.time <= 0f;
        });
        hpSeq.removeAll(hp -> {
            hp.update();
            boolean v = hp.duration <= 0f || hp.e.dead();
            if(v) hp.remove();
            return v;
        });
        if(impFrameTime > 0){
            impFrameTime -= Time.delta;
            if(impFrameTime <= 0f){
                for(ImpactFrameDrawer imf : impFrameEntities){
                    impFramePool.free(imf);
                }
                impFrameEntities.clear();
            }
        }
    }

    static class BuildingHealPrevention extends HealPrevention<Building>{
        @Override
        void update(){
            float h = e.health;
            lastHealth = Math.min(lastHealth, h);
            e.health = lastHealth;
            duration -= Time.delta;
        }

        @Override
        void remove(){
            super.remove();
            bhpPool.free(this);
        }
    }
    static class UnitHealPrevention extends HealPrevention<Unit>{
        @Override
        void update(){
            float h = e.health;
            lastHealth = Math.min(lastHealth, h);
            e.health = lastHealth;
            duration -= Time.delta;
        }

        @Override
        void remove(){
            super.remove();
            uhpPool.free(this);
        }
    }
    static abstract class HealPrevention<T extends Healthc>{
        T e;
        float duration;
        float lastHealth;

        abstract void update();
        void update(float duration, float lastHealth){
            this.duration = Math.max(this.duration, duration);
            this.lastHealth = Math.min(this.lastHealth, lastHealth);
        }
        void remove(){
            hpMap.remove(e.id());
        }
        void set(T t, float duration, float lastHealth){
            e = t;
            this.duration = duration;
            this.lastHealth = lastHealth;
        }
    }

    static class LockMovement{
        Unit u;
        int id;
        float x, y;
        float time = 0f;
    }
    static class ImpactFrameDrawer{
        Runnable run;
        Drawc draw;
        float x, y, rotation;
        boolean directional;
        float intensity;

        void reset(){
            draw = null;
            run = null;
            directional = false;
            rotation = intensity = x = y = 0;
        }
    }

    static class SimplePool<T>{
        private final Seq<T> free = new Seq<>();
        private final Prov<T> prov;

        SimplePool(Prov<T> prov){
            this.prov = prov;
        }

        T obtain(){
            T t = free.isEmpty() ? prov.get() : free.pop();
            return t;
        }

        void free(T t){
            if(t instanceof ImpactFrameDrawer d){
                d.reset();
            }
            free.add(t);
        }
    }
}
