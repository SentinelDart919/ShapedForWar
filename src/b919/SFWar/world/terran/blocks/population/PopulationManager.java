package b919.SFWar.world.terran.blocks.population;

import arc.struct.ObjectMap;
import mindustry.game.Team;

public class PopulationManager {
    /** Used to Access Team Data or Pop. Manager Data*/
    private static final ObjectMap<Team, PopData> data = new ObjectMap<>();
    /** Gives access to PopData */
    private static PopData get(Team team) {
        return data.get(team, PopData::new);
    }
    /** Gives you an int with all the population of the team. THIS GIVES YOU THE POPULATION NO CAPACITY*/
    public static int getPopulation(Team team) {
        return get(team).population;
    }
    /** Gives you an int with all the population CAPACITY of the team. */
    public static int getCapacity(Team team) {
        return get(team).capacity;
    }

    /*public static int getAvailable(Team team) {//decraped old function
        return get(team).population;
    }*/
    /** Adds an amount of population to the team */
    public static void addPopulation(Team team, int amount) {
        get(team).population += amount;
    }
    /** Removes an amount of population to the team */
    public static void removePopulation(Team team, int amount) {
        PopData d = get(team);
        d.population = Math.max(0, d.population - amount);
    }
    /** Adds an amount of population CAPACITY to the team */
    public static void addCapacity(Team team, int amount) {
        get(team).capacity += amount;
    }
    /** Removes an amount of population CAPACITY to the team */
    public static void removeCapacity(Team team, int amount) {
        get(team).capacity = Math.max(0, get(team).capacity - amount);
    }
    //Pop. stuff for Units
    private static final ObjectMap<Integer, Integer> unitIdCost = new ObjectMap<>();
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
        public int population = 0;
        public int capacity = 0;
    }
}
