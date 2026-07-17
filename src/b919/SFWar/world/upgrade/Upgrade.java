package b919.SFWar.world.upgrade;

import arc.Core;
import arc.graphics.g2d.TextureRegion;
import arc.struct.Seq;
import arc.util.Nullable;
import mindustry.type.Item;
import mindustry.type.UnitType;
import mindustry.world.Block;
/** Upgrade*/
public class Upgrade {
    public String name = "";
    public String description = "";

    public @Nullable Item iconItem;
    public @Nullable Block iconBlock;
    public @Nullable UnitType iconUnit;
    public @Nullable String iconRegion;

    public Seq<UpgradeTier> tiers = new Seq<>();
    public Seq<Upgrade> prerequisites = new Seq<>();

    public TextureRegion getDisplayIcon() {
        if(iconItem != null) return iconItem.uiIcon;
        if(iconBlock != null) return iconBlock.uiIcon;
        if(iconUnit != null) return iconUnit.uiIcon;
        if(iconRegion != null) return Core.atlas.find(iconRegion);
        return Core.atlas.find("error");
    }

    public Upgrade name(String name) {
        this.name = name;
        return this;
    }

    public Upgrade desc(String description) {
        this.description = description;
        return this;
    }

    public Upgrade iconItem(Item item) {
        this.iconItem = item;
        return this;
    }

    public Upgrade iconBlock(Block block) {
        this.iconBlock = block;
        return this;
    }

    public Upgrade iconUnit(UnitType unit) {
        this.iconUnit = unit;
        return this;
    }

    public Upgrade iconRegion(String region) {
        this.iconRegion = region;
        return this;
    }

    public Upgrade tiers(UpgradeTier... tiers) {
        this.tiers.addAll(tiers);
        return this;
    }

    public Upgrade prereqs(Upgrade... prereqs) {
        this.prerequisites.addAll(prereqs);
        return this;
    }
}
