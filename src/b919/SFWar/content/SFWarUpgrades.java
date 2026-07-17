package b919.SFWar.content;

import b919.SFWar.world.upgrade.Upgrade;
import b919.SFWar.world.upgrade.UpgradeTier;
import mindustry.content.Items;

import static mindustry.type.ItemStack.with;

public class SFWarUpgrades {
    public static Upgrade damageUpgrade, healthUpgrade;

    public static void load() {
        damageUpgrade = new Upgrade()
            .name("damage-boost")
            .desc("Increases unit damage output")
            .iconItem(Items.copper)
            .tiers(
                new UpgradeTier(with(Items.copper, 20), 1.5f, "Moderate damage increase"),
                new UpgradeTier(with(Items.titanium, 15), 2.0f, "Significant damage increase"),
                new UpgradeTier(with(Items.thorium, 10), 3.0f, "Massive damage increase")
            );

        healthUpgrade = new Upgrade()
            .name("health-boost")
            .desc("Increases unit health")
            .iconItem(Items.lead)
            .prereqs(damageUpgrade)
            .tiers(
                new UpgradeTier(with(Items.lead, 25), 1.5f, "Moderate health increase"),
                new UpgradeTier(with(Items.titanium, 20, Items.lead, 10), 2.0f, "Significant health increase"),
                new UpgradeTier(with(Items.thorium, 15, Items.surgeAlloy, 5), 3.0f, "Massive health increase")
            );
    }
}
