package b919.SFWar.content.blocks;

import arc.struct.Seq;
import b919.SFWar.content.SFWarItems;
import b919.SFWar.world.terran.blocks.population.PopulationHouse;
import b919.SFWar.world.terran.blocks.population.PopulationUnitFactory;
import b919.SFWar.world.terran.blocks.population.RationsDistributor;
import b919.SFWar.world.terran.blocks.storage.TerranCommandCenter;
import b919.SFWar.world.production.SolarCrafter;
import mindustry.content.Items;
import mindustry.content.Liquids;
import mindustry.content.UnitTypes;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.Block;
import mindustry.world.meta.BuildVisibility;

import static mindustry.type.ItemStack.with;

public class TerranBlocks {
    public static Block
            terranCommandCenter,
            terranHouse,
            terranBarracks,
            terranRationsDistributor,
            terranGreenHouse;

    public static void load() {
        terranCommandCenter = new TerranCommandCenter("terran-command-center") {{
            requirements(Category.effect, BuildVisibility.shown, with());
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

        terranHouse = new PopulationHouse("terran-house") {{
            requirements(Category.production, BuildVisibility.shown, with());
            populationCapacity = 4;
            foodItem = SFWarItems.foodRations;
            foodAmount = 1;
            populationTime = 360f;
            size = 2;
            health = 400;
        }};

        terranBarracks = new PopulationUnitFactory("terran-barracks") {{
            requirements(Category.units, BuildVisibility.shown, with());
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

        terranRationsDistributor = new RationsDistributor("terran-rations-distributor") {{
            requirements(Category.distribution, BuildVisibility.shown, with(SFWarItems.steel, 25, SFWarItems.commonAmmo, 10));
            foodItem = SFWarItems.foodRations;
            maxConnections = 8;
            range = 80f;
            transferTime = 60f;
            size = 1;
            health = 400;
        }};

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
    }
}
