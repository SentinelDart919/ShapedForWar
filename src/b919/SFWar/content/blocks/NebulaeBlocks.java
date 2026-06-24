package b919.SFWar.content.blocks;

import mindustry.content.Items;
import mindustry.content.Liquids;
import mindustry.type.Category;
import mindustry.world.Block;
import mindustry.world.blocks.defense.ForceProjector;
import mindustry.world.blocks.production.GenericCrafter;
import mindustry.world.blocks.storage.CoreBlock;
import mindustry.world.meta.BuildVisibility;
import mindustry.type.*; // this will import the classes in this package but not the classes in subpackages
import mindustry.type.unit.*; // this is not for units this is to set entities, not units
import mindustry.content.UnitTypes; //this is for units
import b919.SFWar.content.units.NebulaeUnits;
import b919.SFWar.content.SFWarItems;
import b919.SFWar.content.SFWarLiquids;
import mindustry.world.blocks.power.*;

import static mindustry.type.ItemStack.with;

public class NebulaeBlocks {
    public static Block
            // cores
        domusLucis, castellumLucis, arxLucis,
            // production
        greenStardustPlant, blueStardustPlant, gaseousNyctarPlant,
            // defensive
        luminosityCondenser,
            // offensive
        seminarumStellarum, sartrixNyctar,
            // power
        nyxPanel, nyxPanelSmall;
    public static void load(){

        domusLucis = new CoreBlock("domus-lucis"){{
            requirements(Category.effect, BuildVisibility.shown, with(SFWarItems.solidifiedNyctar, 1));
            health = 10800;
            itemCapacity = 17000;
            size = 6;

            unitCapModifier = 32;
            unitType = UnitTypes.toxopid;
        }};
        castellumLucis = new CoreBlock("castellum-lucis"){{
            requirements(Category.effect, BuildVisibility.shown, with());
            health = 36750;
            itemCapacity = 21000;
            size = 7;

            unitCapModifier = 48;
            unitType = UnitTypes.toxopid;
        }};
        arxLucis = new CoreBlock("arx-lucis"){{
            requirements(Category.effect, BuildVisibility.shown, with());
            health = 96000;
            itemCapacity = 25000;
            size = 8;

            unitCapModifier = 64;
            unitType = UnitTypes.toxopid;
        }};
        luminosityCondenser = new ForceProjector("luminosity-condenser"){{
            requirements(Category.effect, BuildVisibility.shown, with());
            radius = 300f;
            sides = 20;
            shieldHealth = 3000;
            size = 4;
        }};
        nyxPanelSmall = new SolarGenerator("nyx-panel-small"){{
            requirements(Category.power, BuildVisibility.shown, with());
            // reverse solar, basically
            size = 1;
            powerProduction = 0.12f;
        }};
        nyxPanel = new SolarGenerator("nyx-panel"){{
            requirements(Category.power, BuildVisibility.shown, with());
            size = 3;
            powerProduction = 1.6f;
        }};
        greenStardustPlant = new GenericCrafter("green-stardust-plant"){{
            requirements(Category.crafting, with());
            outputItem = new ItemStack(SFWarItems.greenStardust, 1);
            craftTime = 30f;
            size = 2;
            hasPower = true;
            hasLiquids = false;

            consumeItems(with());
            consumePower(0.5f);
        }};
        blueStardustPlant = new GenericCrafter("blue-stardust-plant"){{
            requirements(Category.crafting, with());
            outputItem = new ItemStack(SFWarItems.blueStardust, 1);
            craftTime = 60f;
            liquidCapacity = 60f;
            size = 2;
            hasPower = hasLiquids;
            hasLiquids = true;

            consumeItems(with());
            consumePower(1f);
            consumeLiquid(Liquids.water, 0.1f);
        }};
        gaseousNyctarPlant = new GenericCrafter("gaseous-nyctar-plant"){{
            requirements(Category.crafting, with());
            outputLiquid = new LiquidStack(SFWarLiquids.gaseousNyctar, 6f / 60f);
            craftTime = 60f;
            liquidCapacity = 60f;
            size = 2;
            hasPower = true;
            hasLiquids = true;

            consumeItems(with(Items.coal, 1, SFWarItems.greenStardust, 1));
            consumePower(0.016f);
        }};

    }
}
