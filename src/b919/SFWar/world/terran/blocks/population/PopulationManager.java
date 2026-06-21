package b919.SFWar.world.terran.blocks.population;

import arc.struct.ObjectMap;
import mindustry.game.Team;

public class PopulationManager {
    /** Used to Access Team Data or Pop. Manager Data*/
    private static final ObjectMap<Team, PopData> data = new ObjectMap<>();
    /** Unit cost list*/
    private static final ObjectMap<Integer, Integer> unitIdCost = new ObjectMap<>();
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
    /** Consumes the population (removes amount to available pop) */
    public static void consumePopulation(Team team, int amount) {
        get(team).available -= amount;
    }
    /** Adds available population, this adds raw population to the team, doesn't check if there is pending pop*/
    public static void returnPopulation(Team team, int amount) {
        get(team).available += amount;
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

    //Pop. stuff for Units
    /** Adds Units ID and Unit Population cost to the unitIdCost list*/
    public static void registerUnitIdCost(int unitId, int cost) {
        unitIdCost.put(unitId, cost);
    }
    /** Gets the cost of population of the unit depending on it's id*/
    public static int getUnitIdCost(int unitId) {
        return unitIdCost.get(unitId, 0);
    }
    /** Removes the unit from the list*/
    public static void removeUnitIdCost(int unitId) {
        unitIdCost.remove(unitId);
    }

    /** Clears all population data for all teams. Called when a new world loads */
    public static void clear() {
        data.clear();
        unitIdCost.clear();
    }

    private static class PopData {
        public int capacity = 0;// Capacity, not usable pop
        public int available = 0;// usable pop
        public int pending = 0;// literally negative pop this if your house gets destroyed and you got units
    }
}
