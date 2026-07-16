package b919.SFWar.content.blocks;

import b919.SFWar.content.*;
import b919.SFWar.world.biomass.*;
import mindustry.content.*;
import mindustry.entities.*;
import mindustry.type.*;
import mindustry.world.*;
import mindustry.world.meta.*;

import static mindustry.type.ItemStack.with;

public class BiomassBlocks {
    public static Block
        hive, hiveSpawner, airHiveSpawner, heavyHiveSpawner, behemothSpawner,
        veins,
        biomassGenerator, biomassBulb, fleshHarvester, bloodFilter;

    public static void load() {
        hive = new HiveBlock("hive") {{
            requirements(Category.effect, BuildVisibility.shown, with());
            size = 3;
            itemCapacity = 200000;
            health = 10000;
            unitCapModifier = 32;
            unitType = UnitTypes.alpha;
            minBiomassTime = 60f;
            maxBiomassTime = 240f;
        }};

        hiveSpawner = new HiveUnitSpawner("hive-spawner") {{
            requirements(Category.units, BuildVisibility.shown, with());
            size = 2;
            consumerStacks = new ItemStack[][]{
                new ItemStack[]{new ItemStack(Items.copper, 5)},
                new ItemStack[]{new ItemStack(Items.titanium, 5)},
                new ItemStack[]{new ItemStack(Items.coal, 5)},
            };
            types = new UnitType[]{
                UnitTypes.crawler,
                UnitTypes.fortress,
                UnitTypes.atrax,
            };
            minSpawnTimer = 15f;
            maxSpawnTimer = 45f;
        }};

        airHiveSpawner = new HiveUnitSpawner("air-hive-spawner") {{
            requirements(Category.units, BuildVisibility.shown, with());

            size = 2;
            consumerStacks = new ItemStack[][]{
                new ItemStack[]{new ItemStack(Items.lead, 5)},
                new ItemStack[]{new ItemStack(Items.coal, 5), new ItemStack(Items.copper, 5)},
                new ItemStack[]{new ItemStack(Items.thorium, 5), new ItemStack(Items.scrap, 1)},
            };
            types = new UnitType[]{
                UnitTypes.flare,
                UnitTypes.horizon,
                UnitTypes.zenith,
            };
            minSpawnTimer = 15f;
            maxSpawnTimer = 45f;
        }};

        heavyHiveSpawner = new HiveUnitSpawner("heavy-hive-spawner") {{
            requirements(Category.units, BuildVisibility.shown, with());

            size = 3;
            consumerStacks = new ItemStack[][]{
                new ItemStack[]{new ItemStack(SFWarItems.biomass, 25), new ItemStack(SFWarItems.chromium, 25)},
                new ItemStack[]{new ItemStack(SFWarItems.biomass, 5), new ItemStack(Items.thorium, 10)},
            };
            types = new UnitType[]{
                UnitTypes.pulsar,
                UnitTypes.quasar,
            };
            minSpawnTimer = 30f;
            maxSpawnTimer = 90f;
        }};

        veins = new Veins("veins") {{
            requirements(Category.distribution, BuildVisibility.shown, with());
            size = 1;
            speed = 0.15f;
            itemCapacity = 6;
        }};

        biomassGenerator = new BiomassCrafter("biomass-generator") {{
            requirements(Category.crafting, BuildVisibility.shown, with());
            size = 3;
            squareSprite = false;
            outputItem = new ItemStack(SFWarItems.biomass, 1);
            craftEffect = Fx.smeltsmoke;
            updateEffect = Fx.plasticburn;
        }};

        biomassBulb = new BiomassBulb("biomass-bulb") {{
            requirements(Category.production, BuildVisibility.shown, with());
            size = 2;
            health = 580;
            drillTime = 90;
            tier = 5;
            drawMineItem = true;
            updateEffect = Fx.smeltsmoke;
            drillEffect = Fx.mineBig;
        }};
    }
}
