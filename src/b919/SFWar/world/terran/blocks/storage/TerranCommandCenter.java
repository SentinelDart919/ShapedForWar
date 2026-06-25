package b919.SFWar.world.terran.blocks.storage;

import arc.graphics.Color;
import arc.math.Mathf;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.struct.Seq;
import arc.util.Time;
import arc.util.io.*;
import mindustry.gen.*;
import mindustry.graphics.Pal;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.type.UnitType;
import mindustry.ui.Bar;
import mindustry.ui.Styles;
import mindustry.world.blocks.storage.CoreBlock;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;
import b919.SFWar.world.terran.blocks.population.PopulationManager;
/** CoreBlock with PopulationHouse and PopulationUnitFactory features */
public class TerranCommandCenter extends CoreBlock {
    public int populationCapacity = 0;
    public Seq<UnitPlan> plans = new Seq<>();
    public float produceTime = 600f;

    public boolean requiresFood = true;
    public Item foodItem;
    public int foodAmount = 1;
    public float populationTime = 360f;
    public float populationDecayTime = 600f;

    public TerranCommandCenter(String name) {
        super(name);
        hasItems = true;
        update = true;
        solid = true;
        configurable = true;
        saveConfig = true;
        clearOnDoubleTap = true;
    }

    @Override
    public void init() {
        super.init();
        for (UnitPlan plan : plans) {
            PopulationManager.registerUnitTypeCost(plan.unit.name, plan.populationCost);
        }
    }

    @Override
    public void setStats() {
        super.setStats();
        stats.add(Stat.productionTime, produceTime / 60f, StatUnit.seconds);
        if (populationCapacity > 0) {
            stats.add(Stat.maxUnits, populationCapacity, StatUnit.none);
        }
    }

    @Override
    public void setBars() {
        super.setBars();
        if (populationCapacity > 0) {
            addBar("population", (TerranCommandCenterBuild e) -> new Bar(
                () -> "Population: " + PopulationManager.getPopulation(e.team) + " / " + PopulationManager.getCapacity(e.team),
                () -> Pal.accent,
                () -> (float) PopulationManager.getPopulation(e.team) / Math.max(PopulationManager.getCapacity(e.team), 1)
            ));
        }
        addBar("pop-progress", (TerranCommandCenterBuild e) -> {
            if (e.population >= populationCapacity) return null;
            return new Bar(
                () -> "Progress: " + (int)(e.generationProgress / populationTime * 100) + "%",
                () -> Pal.lighterOrange,
                () -> e.generationProgress / populationTime
            );
        });
        addBar("produce-progress", (TerranCommandCenterBuild e) -> {
            UnitPlan plan = e.getCurrentPlan();
            if (plan == null) return null;
            return new Bar(
                () -> plan.unit.localizedName + " (" + (int)(e.progress / plan.time * 100) + "%)",
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

    public class TerranCommandCenterBuild extends CoreBuild {
        public int population = 0;
        public float generationProgress = 0f;
        public float starvationTimer = 0f;
        public Seq<Integer> queue = new Seq<>();
        public float progress = 0f;

        @Override
        public void created() {
            super.created();
            if (populationCapacity > 0) {
                PopulationManager.addCapacity(team, populationCapacity);
            }
            registerPlanTypeCosts();
        }

        @Override
        public void onRemoved() {
            PopulationManager.onHouseDestroyed(team, populationCapacity, population);
            super.onRemoved();
        }

        @Override
        public void updateTile() {
            super.updateTile();

            if (!requiresFood) {
                if (population < populationCapacity) {
                    generationProgress += Time.delta;
                    if (generationProgress >= populationTime) {
                        generationProgress = 0f;
                        addPopulation(1);
                    }
                }
            } else if (foodItem != null && items.has(foodItem, foodAmount)) {
                starvationTimer = 0f;
                generationProgress += Time.delta;
                if (generationProgress >= populationTime) {
                    generationProgress = 0f;
                    items.remove(foodItem, foodAmount);
                    addPopulation(1);
                }
            } else if (population > 0) {
                starvationTimer += Time.delta;
                if (starvationTimer >= populationDecayTime) {
                    starvationTimer = 0f;
                    removePopulation(1);
                }
            }

            if (queue.size > 0) {
                UnitPlan plan = getCurrentPlan();
                if (plan != null && canProduce(plan)) {
                    progress += edelta();
                }
                assert plan != null;
                if (progress >= plan.time) {
                    progress = 0f;
                    craft(plan);
                    queue.remove(0);
                }
            }
        }

        public void addPopulation(int amount) {
            if (population + amount > populationCapacity) return;
            population += amount;
            PopulationManager.addPopulation(team, amount);
        }

        public void removePopulation(int amount) {
            if (population < amount) return;
            population -= amount;
            PopulationManager.removePopulation(team, amount);
        }

        public UnitPlan getCurrentPlan() {
            if (queue.size == 0) return null;
            return plans.get(queue.first());
        }

        public boolean hasRequirements(UnitPlan plan) {
            if (plan == null) return false;
            if (!PopulationManager.canConsumePopulation(team, plan.populationCost)) return false;
            for (ItemStack req : plan.requirements) {
                if (!items.has(req.item, req.amount)) return false;
            }
            return true;
        }

        public boolean canProduce(UnitPlan plan) {
            if (plan == null) return false;
            return PopulationManager.canConsumePopulation(team, plan.populationCost);
        }

        public void registerPlanTypeCosts() {
            for (UnitPlan p : plans) {
                PopulationManager.registerUnitTypeCost(p.unit.name, p.populationCost);
            }
        }

        public void addToQueue(int planIndex) {
            UnitPlan plan = plans.get(planIndex);
            if (!hasRequirements(plan)) return;
            for (ItemStack req : plan.requirements) {
                items.remove(req.item, req.amount);
            }
            PopulationManager.registerUnitTypeCost(plan.unit.name, plan.populationCost);
            queue.add(planIndex);
        }

        public void craft(UnitPlan plan) {
            PopulationManager.consumePopulation(team, plan.populationCost);
            float angle = rotation * 90f;
            float spawnX = x + Mathf.cosDeg(angle) * (size * 8f + 4f);
            float spawnY = y + Mathf.sinDeg(angle) * (size * 8f + 4f);
            Unit spawned = plan.unit.spawn(team, spawnX, spawnY);
            spawned.rotation(angle);
        }

        public void cancelLast() {
            if (queue.size == 0) return;
            int lastIdx = queue.get(queue.size - 1);
            UnitPlan plan = plans.get(lastIdx);
            for (ItemStack req : plan.requirements) {
                items.add(req.item, req.amount);
            }
            queue.remove(queue.size - 1);
            if (queue.size == 0) {
                progress = 0f;
            }
        }

        @Override
        public void buildConfiguration(Table table) {
            super.buildConfiguration(table);

            table.table(prog -> {
                prog.background(Styles.black6);
                prog.left();
                Label progLabel = new Label("");
                prog.add(progLabel).padLeft(6f).padTop(2f).padBottom(2f);
                prog.update(() -> {
                    UnitPlan plan = getCurrentPlan();
                    if (plan != null) {
                        progLabel.setText(plan.unit.localizedName + " - " + (int)(progress / plan.time * 100) + "%");
                        progLabel.setColor(Pal.lighterOrange);
                    } else {
                        progLabel.setText("Idle");
                        progLabel.setColor(Color.gray);
                    }
                });
            }).fillX().padBottom(4f);

            table.row();

            table.table(btns -> {
                btns.background(Styles.black6);
                btns.left();
                for (int i = 0; i < plans.size; i++) {
                    UnitPlan p = plans.get(i);
                    int idx = i;

                    btns.button(t -> {
                        Stack s = new Stack();
                        s.add(new Image(p.unit.uiIcon));
                        Image x = new Image(Icon.cancel);
                        x.setColor(Color.red);
                        x.visible = false;
                        s.add(x);
                        t.add(s).size(40f);
                    }, () -> addToQueue(idx))
                    .size(50f).pad(2f)
                    .update(b -> {
                        boolean canAfford = hasRequirements(p);
                        b.setDisabled(!canAfford);
                        Stack s = (Stack)b.getChildren().first();
                        s.getChildren().get(0).setColor(canAfford ? Color.white : Color.darkGray);
                        s.getChildren().get(1).visible = !canAfford;
                    })
                    .get()
                    .addListener(new Tooltip(t -> {
                        t.background(Styles.black6);
                        t.defaults().pad(2f);
                        for (ItemStack req : p.requirements) {
                            t.image(req.item.uiIcon).size(20f);
                            t.add(" " + req.amount).padRight(6f);
                        }
                        t.image(Icon.players).size(20f);
                        t.add(" " + p.populationCost);
                    }));

                    if ((i + 1) % 4 == 0) btns.row();
                }
            }).margin(4f);

            table.row();

            table.table(q -> {
                q.background(Styles.black6);
                q.left();
                Label qLabel = new Label("");
                q.add(qLabel).padLeft(6f).padTop(2f).padBottom(2f);
                q.update(() -> {
                    if (queue.size > 0) {
                        qLabel.setText("Queue: " + queue.size + " unit(s)");
                        qLabel.setColor(Color.white);
                    } else {
                        qLabel.setText("No units queued");
                        qLabel.setColor(Color.gray);
                    }
                });
            }).fillX().padTop(4f);

            table.row();

            table.table(cancel -> {
                cancel.right();
                cancel.button(t -> {
                    t.image(Icon.cancel).color(Color.red).size(32f);
                    t.add("Cancel").color(Color.red).padLeft(4f);
                }, this::cancelLast)
                .size(110f, 44f).pad(4f)
                .update(b -> b.setDisabled(queue.size == 0));
            }).fillX().padTop(4f);
        }

        @Override
        public boolean shouldConsume() {
            return queue.size > 0 || population < populationCapacity;
        }


        @Override
        public void write(Writes write) {
            super.write(write);
            write.i(population);
            write.f(generationProgress);
            write.f(starvationTimer);
            write.i(queue.size);
            for (int i = 0; i < queue.size; i++) {
                write.i(queue.get(i));
            }
            write.f(progress);
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);
            population = read.i();
            if (populationCapacity > 0) {
                PopulationManager.addCapacity(team, populationCapacity);
            }
            if (population > 0) {
                PopulationManager.addPopulation(team, population);
            }
            registerPlanTypeCosts();
            generationProgress = read.f();
            starvationTimer = read.f();
            int qsize = read.i();
            queue.clear();
            for (int i = 0; i < qsize; i++) {
                queue.add(read.i());
            }
            progress = read.f();
        }
    }
}
