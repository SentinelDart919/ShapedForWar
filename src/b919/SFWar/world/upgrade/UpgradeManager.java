package b919.SFWar.world.upgrade;

import arc.struct.ObjectMap;
import arc.util.io.Reads;
import arc.util.io.Writes;
/** Upgrade Manager, functions that interacts with upgrades to get/set data and more*/
public class UpgradeManager {
    private static final ObjectMap<String, Integer> currentTiers = new ObjectMap<>();

    public static int getTier(String name) {
        return currentTiers.get(name, 0);
    }

    public static void setTier(String name, int tier) {
        currentTiers.put(name, tier);
    }

    public static boolean isUnlocked(String name) {
        return getTier(name) > 0;
    }

    public static boolean isMaxTier(Upgrade upgrade) {
        return getTier(upgrade.name) >= upgrade.tiers.size;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getData(Upgrade upgrade) {
        int tier = getTier(upgrade.name);
        if(tier <= 0 || tier > upgrade.tiers.size) return null;
        return (T) upgrade.tiers.get(tier - 1).data;
    }

    public static int getInt(Upgrade upgrade, int fallback) {
        Object data = getData(upgrade);
        if(data instanceof Number) return ((Number) data).intValue();
        return fallback;
    }

    public static float getFloat(Upgrade upgrade, float fallback) {
        Object data = getData(upgrade);
        if(data instanceof Number) return ((Number) data).floatValue();
        return fallback;
    }

    public static String getString(Upgrade upgrade, String fallback) {
        Object data = getData(upgrade);
        if(data instanceof String) return (String) data;
        return fallback;
    }

    public static void write(Writes write) {
        int count = 0;
        for(ObjectMap.Entry<String, Integer> entry : currentTiers){
            if(entry.value > 0) count++;
        }
        write.i(count);
        for(ObjectMap.Entry<String, Integer> entry : currentTiers){
            if(entry.value > 0){
                write.str(entry.key);
                write.i(entry.value);
            }
        }
    }

    public static void read(Reads read) {
        currentTiers.clear();
        int count = read.i();
        for(int i = 0; i < count; i++){
            currentTiers.put(read.str(), read.i());
        }
    }

    public static void clear() {
        currentTiers.clear();
    }
}
