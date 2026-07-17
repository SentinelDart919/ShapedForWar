package b919.SFWar;

import arc.Core;
import arc.Events;
import arc.util.Log;
import b919.SFWar.content.*;
import b919.SFWar.content.blocks.BiomassBlocks;
import b919.SFWar.content.blocks.NebulaeBlocks;
import b919.SFWar.content.blocks.SFWarDebugBlocks;
import b919.SFWar.content.blocks.TerranBlocks;
import b919.SFWar.content.units.DebugUnit;
import b919.SFWar.content.units.NebulaeUnits;
import b919.SFWar.content.units.TerranUnits;
import b919.SFWar.ui.PopulationDisplay;
import b919.SFWar.utils.SFWarSFX;
import b919.SFWar.utils.SFWarSounds;
import b919.SFWar.world.terran.blocks.population.PopulationManager;
import b919.SFWar.world.upgrade.UpgradeManager;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.mod.*;

public class SFWarMain extends Mod{

    public SFWarMain(){
        Log.info("Shaped For War loaded.");
        //unit population stuff required, without this it is impossible to remove population when a unit dies, there are simple ways
        //but can be buggy or inefficient,
        Events.on(EventType.WorldLoadBeginEvent.class, e -> {
            PopulationManager.clear();
            UpgradeManager.clear();
        });
        Events.on(EventType.WorldLoadEndEvent.class, e -> Core.app.post(PopulationManager::reloadUnitCosts));
        Events.on(EventType.UnitDestroyEvent.class, e -> {
            int cost = PopulationManager.getUnitTypeCost(e.unit.type.name);
            if (cost > 0) {
                PopulationManager.returnPopulation(e.unit.team(), cost);
            }
        });
        Events.on(EventType.FileTreeInitEvent.class, e -> Core.app.post(SFWarSounds::load));
        Events.on(EventType.ContentInitEvent.class, e -> Core.app.post(() -> {
            if (!Vars.headless) {
                new SFWarSFX();
            }
        }));
    }

    @Override
    public void init(){
        if (!Vars.headless) {
            Core.app.post(PopulationDisplay::init);
        }
    }

    @Override
    public void loadContent(){
        SFWarUpgrades.load();
        TerranUnits.load();
        DebugUnit.load();
        SFWarPlanets.load();
        SFWarItems.load();
        SFWarLiquids.load();
        SFWarBlocks.load();
        BiomassBlocks.load();
        TerranBlocks.load();
        NebulaeUnits.load();
        NebulaeBlocks.load();
        SFWarDebugBlocks.load();
        Log.info("Ready for Action");
    }

}
