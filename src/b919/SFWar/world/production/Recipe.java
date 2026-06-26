package b919.SFWar.world.production;

import arc.scene.ui.layout.*;
import arc.struct.*;
import b919.SFWar.utils.outputs.*;
import mindustry.ctype.UnlockableContent;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.*;
import mindustry.ui.*;
import mindustry.world.*;
import mindustry.world.consumers.*;
import mindustry.world.meta.*;
//Not mine, taken with permission from https://github.com/nullotte/Carpe-Diem/ all credits to the original creator
public class Recipe {
    public float craftTime = 120f;

    public boolean[] itemFilter, liquidFilter;

    public Seq<Consume> consumes = new Seq<>();
    public Seq<Output> outputs = new Seq<>();

    public UnlockableContent primaryOutput;

    public Recipe(float craftTime) {
        this.craftTime = craftTime;
    }

    public void apply(Block block) {
        for (Output output : outputs) {
            output.apply(block);
        }
    }

    public void craft(Building build) {
        for (Output output : outputs) {
            output.trigger(build);
        }
    }

    public void update(Building build) {
        for (Consume consume : consumes) {
            consume.update(build);
        }
        for (Output output : outputs) {
            output.update(build);
        }
    }

    public float efficiencyMultiplier(Building build) {
        float multiplier = 1f;

        for (Consume consume : consumes) {
            multiplier *= consume.efficiencyMultiplier(build);
        }

        return multiplier;
    }

    public void dumpOutputs(Building build) {
        for (Output output : outputs) {
            output.dump(build);
        }
    }

    public void dumpTimedOutputs(Building build) {
        for (Output output : outputs) {
            output.dumpTimed(build);
        }
    }

    public void display(Table table, float craftingSpeed) {
        Stats recipeStats = new Stats();
        recipeStats.timePeriod = craftTime / craftingSpeed;

        for (Consume consume : consumes) {
            consume.display(recipeStats);
        }
        for (Output output : outputs) {
            output.display(recipeStats);
        }

        table.table(Styles.grayPanel, t -> {
            t.table(input -> {
                input.left();

                OrderedMap<Stat, Seq<StatValue>> map = recipeStats.toMap().get(StatCat.crafting);
                Seq<StatValue> arr = map.get(Stat.input);
                if (arr != null) {
                    for (StatValue value : arr) {
                        value.display(input);
                    }
                }
            }).left().grow().pad(10f);

            t.table(arrow -> {
                arrow.image(Icon.right).color(Pal.darkishGray).size(40f);
            }).pad(10f);

            t.table(output -> {
                output.right();

                OrderedMap<Stat, Seq<StatValue>> map = recipeStats.toMap().get(StatCat.crafting);
                Seq<StatValue> arr = map.get(Stat.output);
                if (arr != null) {
                    for (StatValue value : arr) {
                        value.display(output);
                    }
                }
            }).right().grow().pad(10f);
        }).growX().pad(5f);
    }

    public boolean consumesItem(Item item) {
        return itemFilter != null && itemFilter[item.id];
    }

    public boolean consumesLiquid(Liquid liquid) {
        return liquidFilter != null && liquidFilter[liquid.id];
    }

    public boolean valid(Building build) {
        for (Consume consume : consumes) {
            if (!(consume.efficiency(build) > 0f)) {
                return false;
            }
        }

        return true;
    }

    public boolean unlockedNow() {
        return true;
    }

    public boolean shouldConsume(Building build) {
        for (Output output : outputs) {
            if (output.full(build)) return false;
        }

        return true;
    }

    public Recipe consume(Consume consume) {
        consumes.add(consume);
        return this;
    }

    public Recipe output(Output output) {
        outputs.add(output);
        return this;
    }

    public Recipe consumeItem(Item item) {
        return consumeItem(item, 1);
    }

    public Recipe consumeItem(Item item, int amount) {
        return consume(new ConsumeItems(new ItemStack[]{new ItemStack(item, amount)}));
    }

    public Recipe consumeItems(ItemStack... items) {
        return consume(new ConsumeItems(items));
    }

    public Recipe consumeLiquid(Liquid liquid, float amount) {
        return consume(new ConsumeLiquids(new LiquidStack[]{new LiquidStack(liquid, amount)}));
    }

    public Recipe outputItem(Item item) {
        return outputItem(item, 1);
    }

    public Recipe outputItem(Item item, int amount) {
        if (primaryOutput == null) primaryOutput = item;
        return output(new OutputItems(new ItemStack[]{new ItemStack(item, amount)}));
    }

    public Recipe outputItems(ItemStack... items) {
        if (primaryOutput == null && items.length > 0) primaryOutput = items[0].item;
        return output(new OutputItems(items));
    }

    public Recipe outputLiquid(Liquid liquid, float amount) {
        if (primaryOutput == null) primaryOutput = liquid;
        return output(new OutputLiquids(new LiquidStack[]{new LiquidStack(liquid, amount)}));
    }
}
