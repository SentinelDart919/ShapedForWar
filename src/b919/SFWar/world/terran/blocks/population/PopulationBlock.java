package b919.SFWar.world.terran.blocks.population;

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
            if (population > 0) {
                PopulationManager.removePopulation(team, population);
            }
            if (populationCapacity > 0) {
                PopulationManager.removeCapacity(team, populationCapacity);
            }
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

        public boolean addPopulation(int amount) {
            if (population + amount > populationCapacity) return false;
            population += amount;
            PopulationManager.addPopulation(team, amount);
            return true;
        }

        public boolean removePopulation(int amount) {
            if (population < amount) return false;
            population -= amount;
            PopulationManager.removePopulation(team, amount);
            return true;
        }

        @Override
        public boolean shouldConsume() {
            return population < populationCapacity;
        }
    }
}
