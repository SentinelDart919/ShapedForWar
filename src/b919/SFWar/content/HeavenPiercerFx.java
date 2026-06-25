package b919.SFWar.content;

// Extremely embarrassing to make Heaven Pierce Her its own Fx.java
import arc.graphics.Color;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.math.Mathf;
import arc.math.Rand;
import arc.math.geom.Vec2;
import mindustry.entities.Effect;
import mindustry.graphics.Drawf;

import static arc.graphics.g2d.Draw.color;
import static arc.graphics.g2d.Lines.stroke;
import static arc.math.Angles.randLenVectors;

public class HeavenPiercerFx {

    public static final Rand rand = new Rand();
    public static final Vec2 v = new Vec2();

    public static final Effect
            none = new Effect(0, 0f, e -> {}),

    instBombHeaven = new Effect(15f, 100f, e -> {
        color(Color.valueOf("d4240c"));
        stroke(e.fout() * 4f);
        Lines.circle(e.x, e.y, 4f + e.finpow() * 20f);

        for (int i = 0; i < 4; i++) {
            Drawf.tri(e.x, e.y, 6f, 80f * e.fout(), i * 90 + 45);
        }

        color();
        for (int i = 0; i < 4; i++) {
            Drawf.tri(e.x, e.y, 3f, 30f * e.fout(), i * 90 + 45);
        }

        Drawf.light(e.x, e.y, 150f, Color.valueOf("d4240c"), 0.9f * e.fout());
    }),

    instTrailHeaven = new Effect(30, e -> {
        for (int i = 0; i < 2; i++) {
            color(i == 0 ? Color.valueOf("d4240c") : Color.valueOf("ff6e59"));

            float m = i == 0 ? 1f : 0.5f;

            float rot = e.rotation + 180f;
            float w = 15f * e.fout() * m;
            Drawf.tri(e.x, e.y, w, (30f + Mathf.randomSeedRange(e.id, 15f)) * m, rot);
            Drawf.tri(e.x, e.y, w, 10f * m, rot + 180f);
        }

        Drawf.light(e.x, e.y, 60f, Color.valueOf("4684c7"), 0.6f * e.fout());
    }),


    instShootHeaven = new Effect(24f, e -> {
        e.scaled(10f, b -> {
            color(Color.white, Color.valueOf("d4240c"), b.fin());
            stroke(b.fout() * 3f + 0.2f);
            Lines.circle(b.x, b.y, b.fin() * 50f);
        });

        color(Color.valueOf("d4240c"));

        for (int i : Mathf.signs) {
            Drawf.tri(e.x, e.y, 13f * e.fout(), 85f, e.rotation + 90f * i);
            Drawf.tri(e.x, e.y, 13f * e.fout(), 50f, e.rotation + 20f * i);
        }

        Drawf.light(e.x, e.y, 180f, Color.valueOf("d4240c"), 0.9f * e.fout());
    }),

    instHitHeaven = new Effect(20f, 200f, e -> {
        color(Color.valueOf("d4240c"));

        for (int i = 0; i < 2; i++) {
            color(i == 0 ? Color.valueOf("d4240c") : Color.valueOf("ff6e59"));

            float m = i == 0 ? 1f : 0.5f;

            for (int j = 0; j < 5; j++) {
                float rot = e.rotation + Mathf.randomSeedRange(e.id + j, 50f);
                float w = 23f * e.fout() * m;
                Drawf.tri(e.x, e.y, w, (80f + Mathf.randomSeedRange(e.id + j, 40f)) * m, rot);
                Drawf.tri(e.x, e.y, w, 20f * m, rot + 180f);
            }
        }

        e.scaled(10f, c -> {
            color(Color.valueOf("ff6e59"));
            stroke(c.fout() * 2f + 0.2f);
            Lines.circle(e.x, e.y, c.fin() * 30f);
        });

        e.scaled(12f, c -> {
            color(Color.valueOf("d4240c"));
            randLenVectors(e.id, 25, 5f + e.fin() * 80f, e.rotation, 60f, (x, y) -> {
                Fill.square(e.x + x, e.y + y, c.fout() * 3f, 45f);
            });
        });
    });



}

