package b919.SFWar.world.biomass;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.blocks.distribution.Conveyor;

import static mindustry.Vars.*;
import static mindustry.gen.Sounds.none;

public class Veins extends Conveyor {
    public Veins(String name) {
        super(name);
        ambientSound = none;
    }

    public class VeinsConveyorBuild extends ConveyorBuild {
        private static final Color itemColor = Color.valueOf("ae070748");
        public float visualScl = 1f;

        @Override
        public void draw() {
            visualScl = Mathf.lerpDelta(visualScl, len > 0 ? 1.2f : 1f, 0.7f);

            int variant = Mathf.randomSeed(tile.pos(), 0, 3);

            Draw.z(Layer.blockUnder);
            for (int i = 0; i < 4; i++) {
                if ((blending & (1 << i)) != 0) {
                    int dir = rotation - i;
                    float rot = i == 0 ? rotation * 90 : (dir) * 90;
                    Draw.rect(sliced(regions[0][variant], i != 0 ? SliceMode.bottom : SliceMode.top), x + Geometry.d4x(dir) * tilesize * 0.75f, y + Geometry.d4y(dir) * tilesize * 0.75f, rot);
                }
            }

            Draw.z(Layer.block - 0.2f);
            Draw.rect(regions[blendbits][variant], x, y, tilesize * blendsclx * visualScl, tilesize * blendscly * visualScl, rotation * 90);

            Draw.z(Layer.block - 0.1f);
            float layer = Layer.block - 0.1f, wwidth = world.unitWidth(), wheight = world.unitHeight(), scaling = 0.01f;

            for (int i = 0; i < len; i++) {
                Item item = ids[i];
                Tmp.v1.trns(rotation * 90, tilesize, 0);
                Tmp.v2.trns(rotation * 90, -tilesize / 2f, xs[i] * tilesize / 2f);

                float ix = (x + Tmp.v1.x * ys[i] + Tmp.v2.x);
                float iy = (y + Tmp.v1.y * ys[i] + Tmp.v2.y);

                Draw.z(layer + (ix / wwidth + iy / wheight) * scaling);
                Draw.color(itemColor);
                Draw.rect(item.fullIcon, ix, iy, itemSize, itemSize);
                Draw.color();
            }
        }

        @Override
        public void unitOn(Unit unit) {
        }
    }
}
