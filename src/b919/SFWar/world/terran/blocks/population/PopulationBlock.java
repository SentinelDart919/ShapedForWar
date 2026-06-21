package b919.SFWar.world.terran.blocks.population;

import arc.util.io.*;
import mindustry.gen.*;
import mindustry.graphics.Pal;
import mindustry.ui.Bar;
import mindustry.world.Block;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;
/** This is the main class for blocks that Handles population, ofc the other blocks are copy and paste of the vanilla one
 * inheriting this class for population stuff :3 */
public class PopulationBlock extends Block {
    /** Population capacity of the block. THIS INCREASES THE POP. CAPACITY DOESN'T ADD MORE POP.*/
    public int populationCapacity = 0;

    public PopulationBlock(String name) {
        super(name);
        update = true;
        hasItems = true;
        solid = true;
        configurable = true;
        saveConfig = true;
    }

    @Override
    public void setBars() {
        super.setBars();
        if (populationCapacity > 0) {
            addBar("population", (PopulationBuild e) -> new Bar(
                () -> "Population: " + e.population + " / " + populationCapacity,
                () -> Pal.accent,
                () -> (float) e.population / populationCapacity
            ));
        }
    }

    @Override
    public void setStats() {
        super.setStats();
        if (populationCapacity > 0) {
            stats.add(Stat.maxUnits, populationCapacity, StatUnit.none);
        }
    }

    public class PopulationBuild extends Building {
        public int population = 0;

        @Override
        public void created() {
            super.created();
            if (populationCapacity > 0) {
                PopulationManager.addCapacity(team, populationCapacity);
            }
        }

        @Override
        public void onRemoved() {
            PopulationManager.onHouseDestroyed(team, populationCapacity, population);
            super.onRemoved();
        }

        public int getPopulation() {
            return population;
        }

        public int getCapacity() {
            return populationCapacity;
        }

        public int getAvailableCapacity() {
            return populationCapacity - population;
        }

        public void addPopulation(int amount) {
            if (population + amount > populationCapacity) return;
            population += amount;
            PopulationManager.addPopulation(team, amount);
        }

        public void removePopulation(int amount) {
            if (population < amount) return;
            population -= amount;
            PopulationManager.removePopulation(team, amount);
        }

        @Override
        public boolean shouldConsume() {
            return population < populationCapacity;
        }

        @Override
        public void write(Writes write) {
            super.write(write);
            write.i(population);
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);
            population = read.i();
            if (populationCapacity > 0) {
                PopulationManager.addCapacity(team, populationCapacity);
            }
            if (population > 0) {
                PopulationManager.addPopulation(team, population);
            }
        }
    }
}
