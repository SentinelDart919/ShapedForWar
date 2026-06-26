package b919.SFWar.world.biomass;

import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.math.*;
import arc.math.geom.*;
import arc.struct.*;
import arc.util.*;
import arc.util.io.*;
import mindustry.*;
import mindustry.content.*;
import mindustry.entities.units.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.meta.*;

public class HiveUnitSpawner extends Block {
    public UnitType[] types;
    public ItemStack[][] consumerStacks;
    public float minSpawnTimer = 5f;
    public float maxSpawnTimer = 15f;

    public HiveUnitSpawner(String name) {
        super(name);
        update = true;
        solid = false;
        flags = EnumSet.of(BlockFlag.factory);
    }

    protected void spawn(HiveUnitSpawnerBuild build, Building core) {
        int random = Mathf.random(0, types.length - 1);
        ItemStack[] consumer = consumerStacks[Math.min(random, consumerStacks.length - 1)];
        if (core.items.has(consumer)) {
            for (ItemStack stack : consumer) core.items.remove(stack);
            Unit unit = types[random].create(build.team);
            unit.set(build.x + Mathf.range(Vars.tilesize * 2), build.y + Mathf.range(Vars.tilesize * 2));
            unit.add();
        }
    }

    public class HiveUnitSpawnerBuild extends Building {
        public float spawnTimer;

        @Override
        public void draw() {
            super.draw();
            if (team.cores().isEmpty()) return;
            Building core = Geometry.findClosest(x, y, team.cores());
            if (core == null) return;

            Draw.color(Color.valueOf("3c0e0e"));
            Lines.stroke(1f + Mathf.absin(Time.time, 4f, 1f));
            Lines.line(x, y, core.x(), core.y());
            Draw.reset();
        }

        @Override
        public void updateTile() {
            if (team.cores().isEmpty()) return;
            Building core = Geometry.findClosest(x, y, team.cores());
            if (core == null) return;

            spawnTimer += Time.delta;
            if (spawnTimer >= Mathf.random(minSpawnTimer, maxSpawnTimer) * 60f) {
                spawnTimer = 0;
                spawn(this, core);
            }
        }

        @Override
        public void write(Writes write) {
            super.write(write);
            write.f(spawnTimer);
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);
            spawnTimer = read.f();
        }
    }
}
