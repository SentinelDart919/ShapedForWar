package b919.SFWar.world.biomass;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import mindustry.gen.*;
import mindustry.world.blocks.production.Drill;

import static arc.Core.atlas;

public class BiomassBulb extends Drill {
    public TextureRegion topRegion;

    public BiomassBulb(String name) {
        super(name);
        //I would move this to the BiomassBlock class but since is only used once, still can be overridden
        drillTime = 90;
        tier = 5;
    }

    @Override
    public void load() {
        super.load();
        topRegion = atlas.find(name + "-top");
    }

    @Override
    public TextureRegion[] icons() {
        return new TextureRegion[]{region, topRegion};
    }

    public class BiomassBulbBuild extends DrillBuild {
        @Override
        public void draw() {
            float pulse = 1f + Mathf.absin(Time.time, drillTime / 60, 0.15f);

            Draw.rect(region, x, y);
            Draw.rect(topRegion, x, y, pulse * size * 8f, pulse * size * 8f);

            if (dominantItem != null && drawMineItem) {
                Draw.color(dominantItem.color);
                Draw.rect("blank", x, y, 2f, 2f);
                Draw.color();
            }
        }
    }
}
