package b919.SFWar.content.blocks;

import mindustry.type.Category;
import mindustry.world.Block;
import mindustry.world.blocks.defense.ForceProjector;
import mindustry.world.blocks.storage.CoreBlock;
import mindustry.world.meta.BuildVisibility;

import static mindustry.type.ItemStack.with;

public class NebulaeBlocks {
    public static Block
        domusLucis, castellumLucis, arxLucis,
        luminosityCondenser;
    public static void load(){

        domusLucis = new CoreBlock("domus-lucis"){{
            requirements(Category.effect, BuildVisibility.shown, with());
            health = 1500 * size * size;
            itemCapacity = 2000 * size;
            size = 6;
        }};
        castellumLucis = new CoreBlock("castellum-lucis"){{
            requirements(Category.effect, BuildVisibility.shown, with());
            health = 1500 * size * size;
            itemCapacity = 2000 * size;
            size = 7;
        }};
        arxLucis = new CoreBlock("arx-lucis"){{
            requirements(Category.effect, BuildVisibility.shown, with());
            health = 1500 * size * size;
            itemCapacity = 2000 * size;
            size = 8;
        }};
        luminosityCondenser = new ForceProjector("luminosity-condenser"){{
            requirements(Category.effect, BuildVisibility.shown, with());
            radius = 300f;
            sides = 20;
            shieldHealth = 3000;
            size = 4;
        }};

    }
}
