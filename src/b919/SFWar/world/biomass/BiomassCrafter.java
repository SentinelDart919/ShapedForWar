package b919.SFWar.world.biomass;

import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import mindustry.world.blocks.production.GenericCrafter;

public class BiomassCrafter extends GenericCrafter {
    public BiomassCrafter(String name) {
        super(name);
        craftTime = Mathf.random(1290f, 1990f);
        // same as the BiomassBulb
    }

    public class BiomassCrafterBuild extends GenericCrafterBuild {
        @Override
        public void draw() {
            float pulse = 1f + Mathf.absin(Time.time, 10f, 0.15f);
            Draw.scl(pulse * 1.5f);
            Draw.rect(region, x, y, 0/*, pulse * size * 8f, pulse * size * 8f old function*/);
            Draw.scl();
        }
    }
}
