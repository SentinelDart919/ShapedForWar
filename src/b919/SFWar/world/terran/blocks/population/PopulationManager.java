package b919.SFWar.world.terran.blocks.population;

import arc.struct.ObjectMap;
import mindustry.game.Team;

public class PopulationManager {
    private static final ObjectMap<Team, PopData> data = new ObjectMap<>();

    private static PopData get(Team team) {
        return data.get(team, () -> new PopData());
    }

    public static int getPopulation(Team team) {
        return get(team).population;
    }

    public static int getCapacity(Team team) {
        return get(team).capacity;
    }

    /*public static int getAvailable(Team team) {
        return get(team).population;
    }*/

    public static void addPopulation(Team team, int amount) {
        get(team).population += amount;
    }

    public static void removePopulation(Team team, int amount) {
        PopData d = get(team);
        d.population = Math.max(0, d.population - amount);
    }

    public static void addCapacity(Team team, int amount) {
        get(team).capacity += amount;
    }

    public static void removeCapacity(Team team, int amount) {
        get(team).capacity -= amount;
    }

    private static class PopData {
        public int population = 0;
        public int capacity = 0;
    }
}
