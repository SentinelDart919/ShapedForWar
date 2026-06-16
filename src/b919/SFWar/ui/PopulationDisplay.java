package b919.SFWar.ui;

import arc.scene.ui.*;
import arc.scene.ui.layout.Table;
import mindustry.Vars;
import b919.SFWar.world.terran.blocks.population.PopulationManager;
//funky display of population, it's even
public class PopulationDisplay {
    public static void init() {
        if (Vars.headless) return;

        Table popTable = new Table();
        popTable.top().left();
        popTable.setFillParent(true);
        popTable.marginTop(100f).marginLeft(8f);

        Label label = new Label("");

        popTable.add(label).pad(6f);

        popTable.update(() -> {
            if (Vars.player == null || Vars.player.team() == null) return;

            var team = Vars.player.team();
            int population = PopulationManager.getPopulation(team);
            int cap = PopulationManager.getCapacity(team);

            label.setText(
                "[accent]Population:[] " + population + " / " + cap
            );
        });

        Vars.ui.hudGroup.addChild(popTable);
    }
}
