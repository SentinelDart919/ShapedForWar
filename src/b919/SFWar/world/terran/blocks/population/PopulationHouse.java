package b919.SFWar.world.terran.blocks.population;

import arc.graphics.Color;
import arc.graphics.g2d.*;
import arc.struct.Seq;
import arc.util.Time;
import arc.util.io.*;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.Item;
import mindustry.ui.Bar;
import mindustry.world.Tile;

import static arc.Core.*;

public class PopulationHouse extends PopulationBlock {
    public Item foodItem;
    public int foodAmount = 1;
    public float populationTime = 360f;
    public float populationDecayTime = 600f;
    public TextureRegion bottomRegion, topRegion;
    public TextureRegion[] middleRegions;

    public PopulationHouse(String name) {
        super(name);
        hasItems = true;
    }

    @Override
    public void load() {
        super.load();
        bottomRegion = atlas.find(name + "-bottom");
        topRegion = atlas.find(name + "-top");
        if (populationCapacity > 0) {
            middleRegions = new TextureRegion[populationCapacity];
            for (int i = 0; i < populationCapacity; i++) {
                middleRegions[i] = atlas.find(name + "-middle-" + (i + 1));
            }
        }
    }

    @Override
    public void drawBase(Tile tile) {
        float x = tile.drawx(), y = tile.drawy();

        Draw.z(Layer.block - 1f);
        Draw.rect(bottomRegion, x, y);

        Draw.z(Layer.block);
        Draw.rect(region, x, y);

        PopulationHouseBuild build = (PopulationHouseBuild) tile.build;
        if (build != null) {
            for (int i = 0; i < populationCapacity; i++) {
                TextureRegion mid = middleRegions[i];
                if (!mid.found()) continue;
                if (i < build.usedPopulation) {
                    Draw.z(Layer.block + 0.5f);
                    Draw.color(Color.valueOf("ff3434"));
                    Draw.rect(mid, x, y);
                } else if (i < build.population) {
                    Draw.z(Layer.block + 0.5f);
                    Draw.color(Color.valueOf("292dff"));
                    Draw.rect(mid, x, y);
                }
            }
            Draw.color();
        }

        Draw.z(Layer.block + 1f);
        Draw.rect(topRegion, x, y);
    }

    @Override
    public void setBars() {
        super.setBars();
        addBar("pop-progress", (PopulationHouseBuild e) -> {
            if (e.population >= populationCapacity) return null;
            return new Bar(
                () -> "Progress: " + (int) (e.generationProgress / populationTime * 100) + "%",
                () -> Pal.lighterOrange,
                () -> e.generationProgress / populationTime
            );
        });
    }

    public class PopulationHouseBuild extends PopulationBuild {
        public float generationProgress = 0f;
        public float starvationTimer = 0f;
        public Seq<RationsDistributor.RationsDistributorBuild> distributors = new Seq<>();
        public int usedPopulation = 0;

        @Override
        public void updateTile() {
            super.updateTile();

            if (foodItem != null && items.has(foodItem, foodAmount)) {
                starvationTimer = 0f;
                generationProgress += Time.delta;
                if (generationProgress >= populationTime) {
                    generationProgress = 0f;
                    items.remove(foodItem, foodAmount);
                    addPopulation(1);
                }
            } else if (population > 0) {
                starvationTimer += Time.delta;
                if (starvationTimer >= populationDecayTime) {
                    starvationTimer = 0f;
                    removePopulation(1);
                }
            }

            if (usedPopulation > population) {
                usedPopulation = population;
            }
        }
        public void usePopulation(int amount) {
            usedPopulation = Math.min(population, usedPopulation + amount);
        }

        public void returnPopulation(int amount) {
            usedPopulation = Math.max(0, usedPopulation - amount);
        }

        @Override
        public boolean acceptItem(Building source, Item item) {
            return false;
        }

        public void distributorDeliver(Item item, int amount) {
            items.add(item, amount);
        }

        public void addDistributor(RationsDistributor.RationsDistributorBuild dist) {
            if (!distributors.contains(dist)) {
                distributors.add(dist);
            }
        }

        public void removeDistributor(RationsDistributor.RationsDistributorBuild dist) {
            distributors.remove(dist);
        }

        public boolean hasDistributor() {
            return distributors.size > 0;
        }

        @Override
        public void write(Writes write) {
            super.write(write);
            write.f(generationProgress);
            write.f(starvationTimer);
            write.i(usedPopulation);
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);
            generationProgress = read.f();
            starvationTimer = read.f();
            usedPopulation = read.i();
        }
    }
}
