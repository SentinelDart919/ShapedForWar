package b919.SFWar.content;

import arc.graphics.Color;
import mindustry.type.Item;

public class SFWarItems {
    public static Item
            //general items
            uranium,ruby, enrichedUranium, ferrum, steel, chromium, sulfur, gunpowder,
    //terran
            foodRations, heavyAlloy, armorPlates, heavyArmorPlates, smallCase, mediumCase, bigCase,
            commonAmmo, HEAmmo, APAmmo, ArtilleryRounds, uraniumAmmo,
            uraniumPlates, nuclearFuel, depletedNuclearFuel,
    //biomass
            flesh, denseTissues, biomass,
    //Precursors?
    //Nebulae
            nyctoSteel, solidifiedNyctar;
    public static void load(){
        uranium = new Item("uranium", Color.valueOf("000000")){{
            radioactivity = 100;
        }};
        ruby = new Item("ruby", Color.valueOf("000000")){{}};
        enrichedUranium = new Item("enriched-uranium", Color.valueOf("000000")){{}};
        ferrum = new Item("ferrum", Color.valueOf("000000")){{}};
        steel = new Item("steel", Color.valueOf("000000")){{}};
        chromium = new Item("chromium", Color.valueOf("000000")){{}};
        sulfur = new Item("sulfur", Color.valueOf("000000")){{}};
        gunpowder = new Item("gunpowder", Color.valueOf("000000")){{}};
        foodRations = new Item("food-rations", Color.valueOf("000000")){{}};
        heavyAlloy = new Item("heavy-alloy", Color.valueOf("000000")){{}};
        armorPlates = new Item("armor-plates", Color.valueOf("000000")){{}};
        heavyArmorPlates = new Item("heavy-armor-plates", Color.valueOf("000000")){{}};
        uraniumPlates = new Item("uranium-plates", Color.valueOf("000000")){{}};
        smallCase = new Item("small-case", Color.valueOf("000000")){{}};
        mediumCase = new Item("medium-case", Color.valueOf("000000")){{}};
        bigCase = new Item("big-case", Color.valueOf("000000")){{}};
        commonAmmo = new Item("common-ammo", Color.valueOf("000000")){{}};
        HEAmmo = new Item("HE-ammo", Color.valueOf("000000")){{}};
        APAmmo = new Item("AP-ammo", Color.valueOf("000000")){{}};
        ArtilleryRounds = new Item("artillery-rounds", Color.valueOf("000000")){{}};
        uraniumAmmo = new Item("uranium-ammo", Color.valueOf("000000")){{}};
        nuclearFuel = new Item("nuclear-fuel", Color.valueOf("000000")){{}};
        depletedNuclearFuel = new Item("depleted-nuclear-fuel", Color.valueOf("000000")){{}};
        //biomass
        flesh = new Item("flesh", Color.valueOf("000000")){{}};
        denseTissues = new Item("dense-tissues", Color.valueOf("000000")){{}};
        biomass = new Item("biomass", Color.valueOf("000000")){{}};
        //Nebulae
        nyctoSteel = new Item("nycto-steel", Color.valueOf("ab1ae0")){{}};
        solidifiedNyctar = new Item("solidified-nyctar", Color.valueOf("8e00c2")){{}};
    }
}
