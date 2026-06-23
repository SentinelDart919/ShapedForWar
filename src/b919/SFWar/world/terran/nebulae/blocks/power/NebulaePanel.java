package b919.SFWar.world.terran.nebulae.blocks.power;

import arc.math.Mathf;
import mindustry.world.blocks.power.SolarGenerator;

import static mindustry.Vars.state;

public class NebulaePanel extends SolarGenerator {

    public NebulaePanel(String name) {
        super(name);
    }

    public class NebulaePanelBuild extends GeneratorBuild {
        @Override
        public void updateTile() {
            productionEfficiency = enabled ?
                state.rules.solarMultiplier * Mathf.maxZero(1f + state.rules.ambientLight.a) :
                0f;
        }
    }
}

