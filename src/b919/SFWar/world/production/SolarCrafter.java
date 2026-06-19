package b919.SFWar.world.production;

import arc.math.Mathf;
import mindustry.Vars;
import mindustry.graphics.Pal;
import mindustry.ui.Bar;
import mindustry.world.blocks.production.GenericCrafter;
import mindustry.world.meta.Attribute;

public class SolarCrafter extends GenericCrafter {
    public float minEfficiency = 0f;

    public SolarCrafter(String name) {
        super(name);
    }

    @Override
    public void setBars() {
        super.setBars();
        //TODO add this to the god damn bundle
        addBar("solar-efficiency", (SolarCrafterBuild e) -> new Bar(
            () -> String.format("Efficiency: %.0f%%", e.getEffectiveEfficiency() * 100),
            () -> Pal.accent,
                e::getEffectiveEfficiency
        ));
    }

    public class SolarCrafterBuild extends GenericCrafterBuild {
        public float getEffectiveEfficiency() {
            float lightFactor = Vars.state.rules.lighting ? 1f - Vars.state.rules.ambientLight.a : 1f;
            float lightEfficiency = Mathf.maxZero(Vars.state.rules.solarMultiplier * (Attribute.light.env() + lightFactor));
            return Math.max(lightEfficiency, minEfficiency);
        }
        @Override
        public float getProgressIncrease(float baseProgress) {
            return super.getProgressIncrease(baseProgress) * getEffectiveEfficiency();
        }
    }
}
