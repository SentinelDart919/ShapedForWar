package b919.SFWar.world.upgrade;

import arc.struct.Seq;
import mindustry.type.ItemStack;
import mindustry.world.Block;
/** Upgrade but with tiers */

public class UpgradeTier {
    public ItemStack[] requirements = new ItemStack[0];
    public Seq<Block> needs = new Seq<>();
    public Object data;
    public String description = "";

    public UpgradeTier() {}

    public UpgradeTier(ItemStack[] requirements, Object data) {
        this.requirements = requirements;
        this.data = data;
    }

    public UpgradeTier(ItemStack[] requirements, Object data, String description) {
        this.requirements = requirements;
        this.data = data;
        this.description = description;
    }

    public UpgradeTier(ItemStack[] requirements, Object data, Block... needs) {
        this.requirements = requirements;
        this.data = data;
        this.needs.addAll(needs);
    }

    public UpgradeTier(ItemStack[] requirements, Object data, String description, Block... needs) {
        this.requirements = requirements;
        this.data = data;
        this.description = description;
        this.needs.addAll(needs);
    }

    public UpgradeTier needs(Block... blocks) {
        this.needs.addAll(blocks);
        return this;
    }
}
