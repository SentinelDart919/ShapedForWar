package b919.SFWar.world.terran.nebulae.blocks.power;

import arc.math.Mathf;
import mindustry.world.blocks.power.SolarGenerator;
import mindustry.world.meta.Attribute;

import static mindustry.Vars.state;

public class NebulaePanel extends SolarGenerator {

    public NebulaePanel(String name) {
        super(name);
    }

    public class NebulaePanelBuild extends GeneratorBuild {
        @Override
        public void updateTile() {
            float darknessBonus = state.rules.lighting ? state.rules.ambientLight.a : 0f;
            productionEfficiency = enabled ?
                state.rules.solarMultiplier * Mathf.maxZero(Attribute.light.env() + darknessBonus + 1f) :
                1f;
        }
    }
}

