package b919.SFWar.content;

import arc.graphics.Color;
import mindustry.type.Item;

public class SFWarItems {
    public static Item
            //general items
            uranium/*U238*/, enrichedUranium/*U235*/, uraninite/*natural uranium*/, ferrum, steel, chromium, sulfur, gunpowder, ruby,
    //terran
            foodRations, heavyAlloy, armorPlates, heavyArmorPlates, smallCase, mediumCase, bigCase,
            commonAmmo, HEAmmo, APAmmo, ArtilleryRounds, uraniumAmmo,
            uraniumPlates, nuclearFuel, depletedNuclearFuel,
    //biomass
            flesh, denseTissues, biomass,
    //Precursors?
    //Nebulae
            nyctoSteel, solidifiedNyctar, nyctar, crystallizedGreenStardust, greenStardust,
            crystallizedBlueStardust, blueStardust, crystallizedPurpleStardust, purpleStardust,
            crystallizedYellowStardust, yellowStardust,
            redStardust, idealizedStardust, soul,
            heavenPiercingShell;
    public static void load(){
        uraninite = new Item("uraninite", Color.valueOf("77897a")){{//Ore
            hardness = 4;
            radioactivity = 0.05f;
            cost = 2.2f;
        }};
        uranium = new Item("uranium", Color.valueOf("369e46")){{//U238
            hardness = 4;
            radioactivity = 0.15f;
            cost = 2.5f;
        }};
        enrichedUranium = new Item("enriched-uranium", Color.valueOf("000000")){{//U235
            hardness = 4;
            radioactivity = 1.4f;
        }};
        ruby = new Item("ruby", Color.valueOf("000000")){{
            hardness = 2;
        }};
        ferrum = new Item("ferrum", Color.valueOf("000000")){{
            hardness = 1;
        }};
        steel = new Item("steel", Color.valueOf("000000")){{
            hardness = 2;
            cost = 1.2f;
        }};
        chromium = new Item("chromium", Color.valueOf("000000")){{
            hardness = 3;
            cost = 1.4f;
        }};
        sulfur = new Item("sulfur", Color.valueOf("000000")){{
            hardness = 2;
        }};
        gunpowder = new Item("gunpowder", Color.valueOf("000000")){{
            explosiveness = 0.9876f;
            flammability = 0.7f;
        }};
        foodRations = new Item("food-rations", Color.valueOf("000000")){{}};
        heavyAlloy = new Item("heavy-alloy", Color.valueOf("000000")){{
            cost = 1.5f;
        }};
        armorPlates = new Item("armor-plates", Color.valueOf("000000")){{
            cost = 1.2f;
        }};
        heavyArmorPlates = new Item("heavy-armor-plates", Color.valueOf("000000")){{
            cost = 1.4f;
        }};
        uraniumPlates = new Item("uranium-plates", Color.valueOf("000000")){{
            cost = 2.7f;
        }};
        smallCase = new Item("small-case", Color.valueOf("000000")){{}};
        mediumCase = new Item("medium-case", Color.valueOf("000000")){{}};
        bigCase = new Item("big-case", Color.valueOf("000000")){{}};
        commonAmmo = new Item("common-ammo", Color.valueOf("000000")){{
            flammability = 0.8f;
            explosiveness = 0.2f;
        }};
        HEAmmo = new Item("HE-ammo", Color.valueOf("000000")){{
            flammability = 0.95f;
            explosiveness = 1.8f;
        }};
        APAmmo = new Item("AP-ammo", Color.valueOf("000000")){{
            flammability = 0.95f;
            explosiveness = 0.85f;
        }};
        ArtilleryRounds = new Item("artillery-rounds", Color.valueOf("000000")){{
            flammability = 1.9f;
            explosiveness = 1.95f;
        }};
        uraniumAmmo = new Item("uranium-ammo", Color.valueOf("000000")){{
            flammability = 0.95f;
            explosiveness = 1.15f;
        }};
        nuclearFuel = new Item("nuclear-fuel", Color.valueOf("000000")){{
            radioactivity = 2.5f;
        }};
        depletedNuclearFuel = new Item("depleted-nuclear-fuel", Color.valueOf("000000")){{
            radioactivity = 0.25f;
        }};
        //biomass
        flesh = new Item("flesh", Color.valueOf("000000")){{}};
        denseTissues = new Item("dense-tissues", Color.valueOf("000000")){{}};
        biomass = new Item("biomass", Color.valueOf("000000")){{}};
        //Nebulae
        nyctoSteel = new Item("nycto-steel", Color.valueOf("ab1ae0")){{}};
        solidifiedNyctar = new Item("solidified-nyctar", Color.valueOf("8e00c2")){{}};
        crystallizedGreenStardust = new Item("crystallized-green-stardust", Color.valueOf("4bd452")){{}};
        greenStardust = new Item("green-stardust", Color.valueOf("27e436")){{}};
        crystallizedBlueStardust = new Item("crystallized-blue-stardust", Color.valueOf("4684c7")){{}};
        blueStardust = new Item("blue-stardust", Color.valueOf("3d8fe7")){{}};
        heavenPiercingShell = new Item("heaven-piercing-shell", Color.valueOf("8c0000")){{
            explosiveness = 25f;
        }};
        redStardust = new Item("red-stardust", Color.valueOf("ff0000")){{
            explosiveness = 5f;
        }};
        idealizedStardust = new Item("idealized-stardust", Color.valueOf("ffffff")){{}};
        // is meant to be 000000
        nyctar = new Item("nyctar", Color.valueOf("000000")){{}};
        soul = new Item("soul", Color.valueOf("2ccbef")){{}};
        crystallizedPurpleStardust = new Item("crystallized-purple-stardust", Color.valueOf("000000")){{}};
        purpleStardust = new Item("purple-stardust", Color.valueOf("000000")){{}};
        crystallizedYellowStardust = new Item("crystallized-yellow-stardust", Color.valueOf("ffff00")){{}};
        yellowStardust = new Item("yellow-stardust", Color.valueOf("bfbf00")){{}};
    }
}
