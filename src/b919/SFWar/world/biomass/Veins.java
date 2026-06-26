package b919.SFWar.world.biomass;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.util.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.world.blocks.distribution.Duct;

import static mindustry.Vars.*;
import static mindustry.gen.Sounds.none;

public class Veins extends Duct {
    public Veins(String name) {
        super(name);
        ambientSound = none;
    }

    public class VeinsBuild extends DuctBuild {
        private static final Color itemColor = Color.valueOf("ae070748");

        @Override
        public void draw() {
            float rotation = rotdeg();
            int r = this.rotation;

            for (int i = 0; i < 4; i++) {
                if ((blending & (1 << i)) != 0) {
                    int dir = r - i;
                    float rot = i == 0 ? rotation : (dir) * 90;
                    drawAt(x + Geometry.d4x(dir) * tilesize * 0.75f, y + Geometry.d4y(dir) * tilesize * 0.75f, 0, rot, i != 0 ? SliceMode.bottom : SliceMode.top);
                }
            }

            boolean hasItem = current != null;

            if (hasItem) {
                Draw.z(Layer.blockUnder + 0.1f);
                Tmp.v1.set(Geometry.d4x(recDir) * tilesize / 2f, Geometry.d4y(recDir) * tilesize / 2f)
                    .lerp(Geometry.d4x(r) * tilesize / 2f, Geometry.d4y(r) * tilesize / 2f,
                    Mathf.clamp((progress + 1f) / (2f - 1f / speed)));

                Draw.color(itemColor);
                Draw.rect(current.fullIcon, x + Tmp.v1.x, y + Tmp.v1.y, itemSize, itemSize);
                Draw.color();
            }

            float s = hasItem ? 1.2f : 1f;

            Draw.scl(xscl, yscl);
            Draw.z(Layer.blockUnder);
            Draw.rect(sliced(botRegions[blendbits], SliceMode.none), x, y, rotation);

            Draw.z(Layer.blockUnder + 0.2f);
            Draw.color(transparentColor);
            Draw.rect(sliced(botRegions[blendbits], SliceMode.none), x, y, rotation);
            Draw.color();

            Draw.scl(s, s);
            Draw.rect(sliced(topRegions[blendbits], SliceMode.none), x / s, y / s, rotation);
            Draw.reset();
        }
    }
}
