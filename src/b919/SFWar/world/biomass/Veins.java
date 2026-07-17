package b919.SFWar.world.biomass;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.blocks.*;
import mindustry.world.blocks.distribution.Conveyor;

import static arc.Core.atlas;
import static mindustry.Vars.*;
import static mindustry.gen.Sounds.none;

public class Veins extends Conveyor {
    public TextureRegion[] autotileRegions;

    public Veins(String name) {
        super(name);
        ambientSound = none;
    }

    @Override
    public boolean blends(Tile tile, int rotation, int otherx, int othery, int otherrot, Block otherblock) {
        if (otherblock == this) return true;
        return (otherblock.outputsItems() || otherblock.hasItems)
            && lookingAtEither(tile, rotation, otherx, othery, otherrot, otherblock);
    }

    @Override
    public void load() {
        super.load();
        autotileRegions = TileBitmask.load(name);
    }

    public class VeinsConveyorBuild extends ConveyorBuild {
        private static final Color itemColor = Color.valueOf("8c5b3ca6");
        public float visualScl = 1f;

        @Override
        public void draw() {
            visualScl = Mathf.lerpDelta(visualScl, len > 0 ? 1.2f : 1f, 0.7f);

            int bits = 0;
            for (int i = 0; i < 8; i++) {
                Tile other = tile.nearby(Geometry.d8[i]);
                if (other != null && other.build != null && other.build.team == team) {
                    if (blends(tile, rotation, other.x, other.y, other.build.rotation, other.block())) {
                        bits |= (1 << i);
                    }
                }
            }

            Draw.z(Layer.block - 0.2f);
            Draw.rect(autotileRegions[TileBitmask.values[bits]], x, y, tilesize * visualScl, tilesize * visualScl, 0);

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
