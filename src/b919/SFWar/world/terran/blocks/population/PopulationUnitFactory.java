package b919.SFWar.world.terran.blocks.population;

import arc.math.Mathf;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.Structs;
import mindustry.gen.Building;
import mindustry.graphics.Pal;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.type.UnitType;
import mindustry.ui.Bar;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;

public class PopulationUnitFactory extends PopulationBlock {
    public Seq<UnitPlan> plans = new Seq<>();
    public float produceTime = 600f;
    /**Copy and paste of the vanilla UnitFactory but with population usage*/
    //TODO make inheritance for RTS like Factories, with Queue system, maybe also a way to detects units to make the factory always craft X amount of units,
    // so if on of that unit dies it makes a new one
    public PopulationUnitFactory(String name) {
        super(name);
        hasItems = true;
        solid = true;
        configurable = true;
        saveConfig = true;
        clearOnDoubleTap = true;
    }

    @Override
    public void setStats() {
        super.setStats();
        stats.add(Stat.productionTime, produceTime / 60f, StatUnit.seconds);
    }

    @Override
    public void init() {
        super.init();
        int maxCost = 0;
        for (UnitPlan p : plans) {
            if (p.populationCost > maxCost) maxCost = p.populationCost;
        }
        if (populationCapacity < maxCost) populationCapacity = maxCost;
    }

    @Override
    public void setBars() {
        addBar("population", (PopulationUnitFactoryBuild e) -> {
            int used = PopulationManager.getPopulation(e.team);
            int cap = PopulationManager.getCapacity(e.team);
            if (cap <= 0) return null;
            return new Bar(
                () -> "Pop: " + used + " / " + cap,
                () -> Pal.accent,
                () -> (float) used / cap
            );
        });
        addBar("pop-produce", (PopulationUnitFactoryBuild e) -> {
            UnitPlan plan = e.getCurrentPlan();
            if (plan == null) return null;
            return new Bar(
                () -> plan.unit.localizedName + " (" + (int) (e.progress / plan.time * 100) + "%)",
                () -> Pal.lighterOrange,
                () -> e.progress / plan.time
            );
        });
    }

    public static class UnitPlan {
        public UnitType unit;
        public ItemStack[] requirements;
        public float time;
        public int populationCost;

        public UnitPlan(UnitType unit, float time, int populationCost, ItemStack... requirements) {
            this.unit = unit;
            this.time = time;
            this.populationCost = populationCost;
            this.requirements = requirements;
        }
    }

    public class PopulationUnitFactoryBuild extends PopulationBuild {
        public int currentPlan = 0;
        public float progress = 0f;

        public UnitPlan getCurrentPlan() {
            if (plans.size == 0) return null;
            return plans.get(Mathf.clamp(currentPlan, 0, plans.size - 1));
        }

        @Override
        public void updateTile() {
            super.updateTile();

            UnitPlan plan = getCurrentPlan();
            if (plan == null) return;

            if (canProduce(plan)) {
                progress += edelta();
            }

            if (progress >= plan.time) {
                progress = 0f;
                craft(plan);
            }
        }

        public boolean canProduce(UnitPlan plan) {
            if (plan == null) return false;
            if (PopulationManager.getPopulation(team) < plan.populationCost) return false;
            for (ItemStack req : plan.requirements) {
                if (!items.has(req.item, req.amount)) return false;
            }
            return true;
        }

        public void craft(UnitPlan plan) {
            for (ItemStack req : plan.requirements) {
                items.remove(req.item, req.amount);
            }
            PopulationManager.removePopulation(team, plan.populationCost);

            float angle = rotation * 90f;
            float spawnX = x + Mathf.cosDeg(angle) * (size * 8f + 4f);
            float spawnY = y + Mathf.sinDeg(angle) * (size * 8f + 4f);

            plan.unit.spawn(team, spawnX, spawnY);
        }

        @Override
        public void buildConfiguration(Table table) {
            super.buildConfiguration(table);

            if (plans.size <= 1) return;

            table.row();
            table.table(btns -> {
                for (int i = 0; i < plans.size; i++) {
                    UnitPlan p = plans.get(i);
                    int idx = i;
                    btns.button(t -> t.image(p.unit.uiIcon).size(40f), () -> {
                        currentPlan = idx;
                        progress = 0f;
                    }).size(50f).pad(2f);
                    if ((i + 1) % 4 == 0) btns.row();
                }
            });
        }

        @Override
        public boolean shouldConsume() {
            UnitPlan plan = getCurrentPlan();
            return plan != null && canProduce(plan);
        }

        @Override
        public boolean acceptItem(Building source, Item item) {
            return currentPlan != -1 && items.get(item) < getMaximumAccepted(item) &&
                    Structs.contains(plans.get(currentPlan).requirements, stack -> stack.item == item);
        }
    }
}
