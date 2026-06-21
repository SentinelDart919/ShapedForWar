package b919.SFWar.world.terran.blocks.population;

import arc.struct.Seq;
import arc.util.Time;
import arc.util.io.*;
import mindustry.gen.*;
import mindustry.graphics.Pal;
import mindustry.type.Item;
import mindustry.ui.Bar;

public class PopulationHouse extends PopulationBlock {
    public Item foodItem;
    public int foodAmount = 1;
    public float populationTime = 360f;
    public float populationDecayTime = 600f;

    public PopulationHouse(String name) {
        super(name);
        hasItems = true;
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
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);
            generationProgress = read.f();
            starvationTimer = read.f();
        }
    }
}
