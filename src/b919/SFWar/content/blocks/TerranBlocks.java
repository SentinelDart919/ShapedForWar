package b919.SFWar.content.blocks;

import arc.struct.Seq;
import b919.SFWar.content.SFWarItems;
import b919.SFWar.world.terran.blocks.population.PopulationHouse;
import b919.SFWar.world.terran.blocks.population.PopulationUnitFactory;
import b919.SFWar.world.terran.blocks.population.RationsDistributor;
import mindustry.content.UnitTypes;
import mindustry.type.Category;
import mindustry.type.ItemStack;
import mindustry.world.Block;
import mindustry.world.meta.BuildVisibility;

import static mindustry.type.ItemStack.with;

public class TerranBlocks {
    public static Block
            terranHouse,
            terranBarracks,
            terranRationsDistributor;

    public static void load() {
        terranHouse = new PopulationHouse("terran-house") {{
            requirements(Category.production, BuildVisibility.shown, with());
            populationCapacity = 4;
            foodItem = SFWarItems.foodRations;
            foodAmount = 1;
            populationTime = 360f;
            size = 1;
            health = 400;
        }};

        terranBarracks = new PopulationUnitFactory("terran-barracks") {{
            requirements(Category.units, BuildVisibility.shown, with());
            plans = Seq.with(
                    new PopulationUnitFactory.UnitPlan(UnitTypes.dagger, 600f, 1,
                            ItemStack.with(SFWarItems.steel, 15, SFWarItems.commonAmmo, 10))
            );
            itemCapacity = 60;
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
    }
}
