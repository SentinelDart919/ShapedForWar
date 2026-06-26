package b919.SFWar.world.biomass;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.util.*;
import arc.util.io.*;
import b919.SFWar.content.SFWarItems;
import mindustry.gen.*;
import mindustry.world.blocks.storage.*;

public class HiveBlock extends CoreBlock {
    public float minBiomassTime = 60f;
    public float maxBiomassTime = 240f;

    public HiveBlock(String name) {
        super(name);
    }

    public class HiveBuild extends CoreBuild {
        public float biomassTimer;
        public float biomassGoal = 210f;

        @Override
        public void draw() {
            float pulse = 1f + Mathf.absin(Time.time, 4f, 0.05f);
            Draw.rect(region, x, y, pulse * size * 8f, pulse * size * 8f);
        }

        @Override
        public void updateTile() {
            super.updateTile();

            biomassTimer += Time.delta / 60f;
            if (biomassTimer >= biomassGoal) {
                biomassTimer = 0;
                biomassGoal = Mathf.random(minBiomassTime, maxBiomassTime);
                items.add(SFWarItems.biomass, Mathf.random(1, 3));
            }
        }

        @Override
        public void write(Writes write) {
            super.write(write);
            write.f(biomassTimer);
            write.f(biomassGoal);
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);
            biomassTimer = read.f();
            biomassGoal = read.f();
        }
    }
}
