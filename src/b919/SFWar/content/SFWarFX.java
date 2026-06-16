package b919.SFWar.content;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import mindustry.*;
import mindustry.entities.*;
import mindustry.graphics.*;

import static arc.math.Interp.*;
import static arc.graphics.g2d.Draw.*;

public class SFWarFX{
    public static Effect desNukeShockwave = new Effect(190f, 1900f * 2f, e -> {
        float size = e.rotation;

        color(Color.white, 0.333f * e.fout());
        Lines.stroke((size / 15f) + (size / 5f) * e.fin());
        Lines.circle(e.x, e.y, size / 3f + size * pow2Out.apply(e.fin()) * 2f);
    }).layer(Layer.groundUnit + 1f),

    desNuke = new Effect(80f, 500f * 2, e -> {
        if(!(e.data instanceof float[] arr)) return;
        float size = e.rotation;

        Rand r = SFWarUtils.rand;
        r.setSeed(e.id);

        float scl = 1f;
        Tmp.c2.set(Color.gray).a(0.8f);
        for(int k = 0; k < 6; k++){
            float cf = k / 5f;
            color(Tmp.c2, Pal.lightOrange, cf);
            for(int i = 0; i < 40; i++){
                float f = Mathf.curve(e.fin(), 0f, 1f - r.random(0.2f));
                float len = r.random(size * scl * 0.75f) * pow5Out.apply(f) + r.random(size / 5f);
                float ang = r.random(360f);
                float psize = size / 5f;
                float rad = r.random(psize * (scl * 0.5f + 0.5f) * 0.87f, psize) * scl * (1f - pow5In.apply(f));
                if(f < 1f){
                    Tmp.v1.trns(ang, len).add(e.x, e.y);
                    Fill.circle(Tmp.v1.x, Tmp.v1.y, rad);
                }
            }
            scl *= 0.75f;
        }
        scl = 1f;
        color(Pal.lighterOrange);
        Lines.stroke(3f);
        for(int i = 0; i < 4; i++){
            for(int j = 0; j < 20; j++){
                float f = Mathf.curve(e.fin(), 0f, 1f - r.random(0.2f));
                float ang = r.random(360f);
                float len = r.random(size * scl * 0.5f) * pow5Out.apply(f) + r.random(size / 5f);
                float line = r.random(22f, 45f) * Mathf.pow(scl, 1.1f) * pow2Out.apply(Mathf.slope(pow5Out.apply(f)));

                if(f < 1f){
                    Tmp.v1.trns(ang, len).add(e.x, e.y);
                    Lines.lineAngle(Tmp.v1.x, Tmp.v1.y, ang, line, false);
                }
            }
            scl *= 1.4f;
        }

        float fin = Mathf.clamp(e.time / 10f);
        if(fin < 1){
            Tmp.c2.set(Pal.lightOrange).a(0f);
            color(Pal.lighterOrange, Tmp.c2, fin);
            for(int i = 0; i < arr.length; i++){
                float len1 = arr[i], len2 = arr[(i + 1) % arr.length];
                float ang1 = (i / (float)arr.length) * 360f;
                float ang2 = ((i + 1f) / arr.length) * 360f;

                if(len1 >= size){
                    len1 += (size / 1.5f) * fin;
                }
                if(len2 >= size){
                    len2 += (size / 1.5f) * fin;
                }

                float x1 = Mathf.cosDeg(ang1) * len1, y1 = Mathf.sinDeg(ang1) * len1;
                float x2 = Mathf.cosDeg(ang2) * len2, y2 = Mathf.sinDeg(ang2) * len2;

                Fill.tri(e.x, e.y, e.x + x1, e.y + y1, e.x + x2, e.y + y2);
            }
        }
    }),

    desNukeShoot = new Effect(35f, e -> {
        float ang = 90f, len = 1f;
        Rand r = SFWarUtils.rand;
        r.setSeed(e.id);

        Lines.stroke(2f);
        for(int i = 0; i < 5; i++){
            for(int j = 0; j < 7; j++){
                float f = Mathf.curve(e.fin(), 0f, 1f - r.random(0.2f));
                float rot = e.rotation + r.range(ang);
                Draw.color(SFWarPal.red, Color.white, f);
                Vec2 v = Tmp.v1.trns(rot, r.random(40f) * pow2Out.apply(f) * len).add(e.x, e.y);
                Lines.lineAngle(v.x, v.y, rot, f * 40f * r.random(0.75f, 1f) * len * pow2Out.apply(Mathf.slope(f)), false);
            }
            ang *= 0.5f;
            len *= 1.4f;
        }
    }),

    desNukeVaporize = new Effect(40f, 1200f, e -> {
        float size = e.data instanceof Float ? (float)e.data : 10f;

        Rand r = SFWarUtils.rand;
        r.setSeed(e.id);

        int count = 20 + (int)(size * size * 0.5f);
        float c = 0.25f;
        for(int i = 0; i < count; i++){
            float l = r.nextFloat() * c;
            float f = Mathf.curve(e.fin(), l, ((1f - c) + l) * r.random(0.8f, 1f));
            float len = r.random(0.5f, 1f) * (80f + size * 10f) * pow2In.apply(f);
            float off = Mathf.sqrt(r.nextFloat()) * size, ang = r.random(360f), rng = r.range(10f);
            float scl = (size / 2f) * r.random(0.9f, 1.1f) * biasSlope(f, 0.1f);

            if(f > 0 && f < 1){
                Vec2 v1 = Tmp.v1.trns(ang, off).add(e.x, e.y).add(Tmp.v2.trns(e.rotation + rng, len));
                Draw.color(Pal.lightOrange, Pal.rubble, pow3Out.apply(f));
                Fill.circle(v1.x, v1.y, scl);
            }
        }
    }).layer(Layer.flyingUnit),

    desNukeShockSmoke = new Effect(40f, 800f, e -> {
        Rand r = SFWarUtils.rand;
        r.setSeed(e.id);

        int count = 10;
        float c = 0.4f;
        for(int i = 0; i < count; i++){
            float l = r.nextFloat() * c;
            float f = Mathf.curve(e.fin(), l, ((1f - c) + l) * r.random(0.8f, 1f));
            float len = r.random(0.75f, 1f) * 160f * pow2In.apply(f);
            float off = Mathf.sqrt(r.nextFloat()) * Vars.tilesize / 2f, ang = r.random(360f), rng = r.range(10f);
            float scl = r.random(4f, 6f) * (1f - pow2In.apply(f));

            if(f > 0 && f < 1){
                Vec2 v1 = Tmp.v1.trns(ang, off).add(e.x, e.y).add(Tmp.v2.trns(e.rotation + rng, len));
                color(Pal.rubble, Color.gray, f);
                Fill.circle(v1.x, v1.y, scl);
            }
        }
    });

    static float biasSlope(float fin, float bias){
        return (fin < bias ? (fin / bias) : 1f - (fin - bias) / (1f - bias));
    }
}
