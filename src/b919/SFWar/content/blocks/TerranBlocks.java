package b919.SFWar.content.blocks;

import arc.struct.Seq;
import b919.SFWar.content.SFWarItems;
import b919.SFWar.world.terran.blocks.population.PopulationHouse;
import b919.SFWar.world.terran.blocks.population.PopulationUnitFactory;
import b919.SFWar.world.terran.blocks.population.RationsDistributor;
import b919.SFWar.world.terran.blocks.storage.TerranCommandCenter;
import b919.SFWar.world.distribution.UndergroundBelt;
import b919.SFWar.world.production.SolarCrafter;
import mindustry.content.Items;
import mindustry.content.Liquids;
import mindustry.content.UnitTypes;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.Block;
import mindustry.world.blocks.distribution.*;
import mindustry.world.meta.BuildVisibility;

import static mindustry.type.ItemStack.with;

public class TerranBlocks {
    public static Block
            terranCommandCenter,
            terranHouse,
            terranBarracks,
            //distribution
            terranBasicConveyor, terranArmoredConveyor, terranAdvancedConveyor, terranAdvancedArmoredConveyor,
            terranJunction, terranNecessaryEvil, terranRationsDistributor,
            terranUndergroundBeltT1, terranUndergroundBeltT2, terranUndergroundBeltT3,
            //crafting and production

            terranGreenHouse;
            //power related

    public static void load() {
        //Core
        terranCommandCenter = new TerranCommandCenter("terran-command-center") {{
            requirements(Category.effect, BuildVisibility.shown, with(SFWarItems.ferrum, 5000, Items.lead, 5000, SFWarItems.steel, 5000, SFWarItems.heavyAlloy, 5000));
            populationCapacity = 8;
            requiresFood = false;
            populationTime = 360f;
            plans = Seq.with(
                    new UnitPlan(UnitTypes.mono, 600f, 1,
                            ItemStack.with(Items.copper, 15, Items.lead, 10)),
                    new UnitPlan(UnitTypes.poly, 800f, 1,
                            ItemStack.with(Items.copper, 30, Items.lead, 60))
            );
            size = 5;
            health = 6000;
            unitType = UnitTypes.mega;
            itemCapacity = 8000;
            unitCapModifier = 32;
        }};

        //Population
        terranHouse = new PopulationHouse("terran-house") {{
            requirements(Category.production, BuildVisibility.shown, with(SFWarItems.ferrum, 50, Items.lead, 25));
            populationCapacity = 4;
            foodItem = SFWarItems.foodRations;
            foodAmount = 1;
            populationTime = 360f;
            size = 2;
            health = 400;
        }};

        //Units
        terranBarracks = new PopulationUnitFactory("terran-barracks") {{
            requirements(Category.units, BuildVisibility.shown, with(SFWarItems.ferrum, 150, Items.lead, 200));
            plans = Seq.with(
                    new UnitPlan(UnitTypes.dagger, 600f, 1,
                            ItemStack.with(SFWarItems.steel, 15, SFWarItems.commonAmmo, 10)),
                    new UnitPlan(UnitTypes.nova, 800f, 1,
                            ItemStack.with(SFWarItems.armorPlates, 30, SFWarItems.commonAmmo, 20)),
                    new UnitPlan(UnitTypes.mace, 1000f, 2,
                            ItemStack.with(SFWarItems.armorPlates, 45, Items.pyratite, 30))
            );
            size = 2;
            health = 800;
        }};

        //Item Distribution related
        terranBasicConveyor = new Conveyor("terran-basic-conveyor"){{
            requirements(Category.distribution, BuildVisibility.shown, with(SFWarItems.ferrum, 1));
            health = 65;
            speed = 0.08f;
            displayedSpeed = 11f;
        }};
        terranArmoredConveyor = new ArmoredConveyor("terran-armored-conveyor"){{
            requirements(Category.distribution, BuildVisibility.shown, with(SFWarItems.ferrum, 2, SFWarItems.steel, 1));
            health = 240;
            speed = 0.08f;
            displayedSpeed = 11f;
        }};
        terranAdvancedConveyor = new Conveyor("terran-advanced-conveyor"){{
            requirements(Category.distribution, BuildVisibility.shown, with(SFWarItems.steel, 2, SFWarItems.heavyAlloy, 1));
            health = 130;
            speed = 0.15f;
            displayedSpeed = 18f;
        }};
        terranAdvancedArmoredConveyor = new Conveyor("terran-advanced-armored-conveyor"){{
            requirements(Category.distribution, BuildVisibility.shown, with(SFWarItems.uraniumPlates, 1, SFWarItems.steel, 2, SFWarItems.heavyAlloy, 1));
            health = 570;
            speed = 0.15f;
            displayedSpeed = 18f;
        }};
        terranNecessaryEvil = new Router("terran-router"){{
            requirements(Category.distribution, BuildVisibility.shown, with(SFWarItems.ferrum, 2));
        }};
        terranJunction = new Junction("terran-junction"){{
            requirements(Category.distribution, BuildVisibility.shown, with(SFWarItems.ferrum, 2));
        }};
        terranRationsDistributor = new RationsDistributor("terran-rations-distributor") {{
            requirements(Category.distribution, BuildVisibility.shown, with(SFWarItems.steel, 25, SFWarItems.commonAmmo, 10));
            foodItem = SFWarItems.foodRations;
            maxConnections = 8;
            range = 80f;
            transferTime = 60f;
            size = 1;
            health = 400;
        }};
        terranUndergroundBeltT1 = new UndergroundBelt("terran-underground-belt-t1"){{
            requirements(Category.distribution, BuildVisibility.shown, with(SFWarItems.ferrum, 5, Items.lead, 2));
            range = 7;
            depth = 1;
            health = 100;
        }};
        terranUndergroundBeltT2 = new UndergroundBelt("terran-underground-belt-t2"){{
            requirements(Category.distribution, BuildVisibility.shown, with(SFWarItems.steel, 10, SFWarItems.ferrum, 5));
            range = 5;
            depth = 2;
            health = 150;
        }};
        terranUndergroundBeltT3 = new UndergroundBelt("terran-underground-belt-t3"){{
            requirements(Category.distribution, BuildVisibility.shown, with(SFWarItems.heavyAlloy,5, SFWarItems.steel, 10));
            range = 3;
            depth = 3;
            health = 200;
        }};

        //Item crafting
        terranGreenHouse = new SolarCrafter("terran-green-house"){{
            requirements(Category.production, BuildVisibility.shown, with());
            outputItem = new ItemStack(SFWarItems.foodRations, 1);
            size = 2;
            health = 400;
            minEfficiency = 0;
            hasItems = true;
            consumeLiquid(Liquids.water, 0.45f / 60f);
            consumePower(2f);
        }};

        //Power Related
    }
}
