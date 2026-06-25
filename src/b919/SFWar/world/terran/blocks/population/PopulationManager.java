package b919.SFWar.world.terran.blocks.population;

import arc.struct.ObjectMap;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Groups;

public class PopulationManager {
    /** Used to Access Team Data or Pop. Manager Data*/
    private static final ObjectMap<Team, PopData> data = new ObjectMap<>();
    /** Unit type cost list (unittype name -> cost)*/
    private static final ObjectMap<String, Integer> unitTypeCost = new ObjectMap<>();
    /** Gives access to PopData */
    private static PopData get(Team team) {
        return data.get(team, PopData::new);
    }
    /** Gives you an int with all the population of the team. THIS GIVES YOU THE POPULATION NO CAPACITY*/
    public static int getPopulation(Team team) {
        return get(team).available;
    }
    /** Gives you an int with all the population CAPACITY of the team. */
    public static int getCapacity(Team team) {
        return get(team).capacity;
    }
    /** Gives you an int with all the population PENDING of the team. */

    public static int getPending(Team team) {
        return get(team).pending;
    }
    /** Adds an amount of population to the team */
    public static void addPopulation(Team team, int amount) {
        PopData d = get(team);
        for (int i = 0; i < amount; i++) {
            if (d.pending > 0) {
                d.pending--;
            } else if (d.available < d.capacity) {
                d.available++;
            }
        }
    }
    /** Removes an amount of population to the team */
    public static void removePopulation(Team team, int amount) {
        PopData d = get(team);
        d.available = Math.max(0, d.available - amount);
    }
    /** Checks if you can consume Population (available pop should be > than amount) */
    public static boolean canConsumePopulation(Team team, int amount) {
        return get(team).available >= amount;
    }
    /** Consumes the population (removes amount to available pop and marks house pop as used) */
    public static void consumePopulation(Team team, int amount) {
        get(team).available -= amount;
        //funky, I made this originally for a drawer lol
        int remaining = amount;
        for (Building b : Groups.build) {
            if (b instanceof PopulationHouse.PopulationHouseBuild house && b.team == team && house.population > house.usedPopulation) {
                int toUse = Math.min(house.population - house.usedPopulation, remaining);
                house.usePopulation(toUse);
                remaining -= toUse;
                if (remaining <= 0) break;
            }
        }
    }
    /** Adds available population, this adds raw population to the team, doesn't check if there is pending pop*/
    public static void returnPopulation(Team team, int amount) {
        get(team).available += amount;
        int remaining = amount;
        for (Building b : Groups.build) {
            if (b instanceof PopulationHouse.PopulationHouseBuild house && b.team == team && house.usedPopulation > 0) {
                int toReturn = Math.min(house.usedPopulation, remaining);
                house.returnPopulation(toReturn);
                remaining -= toReturn;
                if (remaining == 0) break;
            }
        }
    }
    /** Adds population capacity*/
    public static void addCapacity(Team team, int amount) {
        get(team).capacity += amount;
    }
    /** Removes the population capacity and available population of the house*/
    public static void onHouseDestroyed(Team team, int houseCap, int houseGen) {
        PopData d = get(team);
        d.capacity -= houseCap;
        if (houseGen > 0) {
            int removedAvailable = Math.min(d.available, houseGen);
            d.available -= removedAvailable;
            d.pending += houseGen - removedAvailable;
        }
    }

    /** Registers a unit types population cost by type name*/
    public static void registerUnitTypeCost(String typeName, int cost) {
        unitTypeCost.put(typeName, cost);
    }

    /** Gets the population cost for a unit type by name(string)*/
    public static int getUnitTypeCost(String typeName) {
        return unitTypeCost.get(typeName, 0);
    }

    /** Re-registers population consumption for all alive units after map load*/
    public static void reloadUnitCosts() {
        Groups.unit.each(u -> {
            int cost = unitTypeCost.get(u.type.name, 0);
            if (cost > 0) {
                consumePopulation(u.team, cost);
            }
        });
    }

    /** Clears all population data for all teams. Called when a new world loads */
    public static void clear() {
        data.clear();
    }

    private static class PopData {
        public int capacity = 0;// Capacity, not usable pop
        public int available = 0;// usable pop
        public int pending = 0;// literally negative pop this if your house gets destroyed and you got units
    }
}
