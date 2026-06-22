package b919.SFWar.world.terran.blocks.population;

import arc.graphics.Color;
import arc.math.Mathf;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.struct.Seq;
import arc.util.io.*;
import mindustry.gen.*;
import mindustry.graphics.Pal;
import mindustry.type.Item;
import mindustry.type.ItemStack;
import mindustry.type.UnitType;
import mindustry.ui.Bar;
import mindustry.ui.Styles;
import mindustry.world.meta.Stat;
import mindustry.world.meta.StatUnit;

public class PopulationUnitFactory extends PopulationBlock {
    public Seq<UnitPlan> plans = new Seq<>();
    public float produceTime = 600f;
    /**Copy and paste of the vanilla UnitFactory but with population(actually steroids) usage*/
    public PopulationUnitFactory(String name) {
        super(name);
        hasItems = false;
        rotate = true;
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
        public Seq<Integer> queue = new Seq<>();
        public float progress = 0f;

        public UnitPlan getCurrentPlan() {
            if (queue.size == 0) return null;
            return plans.get(queue.first());
        }

        @Override
        public void updateTile() {
            super.updateTile();
            if (queue.size == 0) return;

            UnitPlan plan = getCurrentPlan();
            if (plan == null) return;

            if (canProduce(plan)) {
                progress += edelta();
            }

            if (progress >= plan.time) {
                progress = 0f;
                craft(plan);
                queue.remove(0);
            }
        }

        public boolean hasRequirements(UnitPlan plan) {
            if (plan == null) return false;
            if (!PopulationManager.canConsumePopulation(team, plan.populationCost)) return false;

            Building core = team.core();
            if (core == null) return false;

            for (ItemStack req : plan.requirements) {
                if (!core.items.has(req.item, req.amount)) return false;
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

        @Override
        public void created() {
            super.created();
            registerPlanTypeCosts();
        }

        public boolean addToQueue(int planIndex) {
            UnitPlan plan = plans.get(planIndex);
            if (!hasRequirements(plan)) return false;

            Building core = team.core();
            if (core == null) return false;

            for (ItemStack req : plan.requirements) {
                core.items.remove(req.item, req.amount);
            }

            PopulationManager.registerUnitTypeCost(plan.unit.name, plan.populationCost);
            queue.add(planIndex);
            return true;
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

            Building core = team.core();
            if (core != null) {
                for (ItemStack req : plan.requirements) {
                    core.items.add(req.item, req.amount);
                }
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
            return queue.size > 0;
        }

        @Override
        public boolean acceptItem(Building source, Item item) {
            return false;
        }

        @Override
        public void write(Writes write) {
            super.write(write);
            write.i(queue.size);
            for (int i = 0; i < queue.size; i++) {
                write.i(queue.get(i));
            }
            write.f(progress);
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);
            registerPlanTypeCosts();
            int qsize = read.i();
            queue.clear();
            for (int i = 0; i < qsize; i++) {
                queue.add(read.i());
            }
            progress = read.f();
        }
    }
}
