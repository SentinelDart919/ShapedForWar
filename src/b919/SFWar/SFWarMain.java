package b919.SFWar;

import arc.Core;
import arc.Events;
import arc.util.Log;
import b919.SFWar.content.*;
import b919.SFWar.content.blocks.NebulaeBlocks;
import b919.SFWar.content.blocks.SFWarDebugBlocks;
import b919.SFWar.content.blocks.TerranBlocks;
import b919.SFWar.ui.PopulationDisplay;
import b919.SFWar.utils.SFWarSFX;
import b919.SFWar.utils.SFWarSounds;
import b919.SFWar.world.terran.blocks.population.PopulationHouse;
import b919.SFWar.world.terran.blocks.population.PopulationManager;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.mod.*;

public class SFWarMain extends Mod{

    public SFWarMain(){
        Log.info("Shaped For War loaded.");
        Events.on(EventType.WorldLoadBeginEvent.class, e -> PopulationManager.clear());
        //unit population stuff required, without this it is impossible to remove population when a unit dies, there are simple ways
        //but can be buggy or inefficient,
        Events.on(EventType.UnitDestroyEvent.class, e -> {
            int cost = PopulationManager.getUnitIdCost(e.unit.id);
            if (cost > 0) {
                PopulationManager.removeUnitIdCost(e.unit.id);
                for (PopulationHouse.PopulationHouseBuild house : PopulationHouse.getHouses(e.unit.team())) {
                    if (house.population >= cost) {
                        house.removePopulation(cost);
                        cost = 0;
                        break;
                    }
                }
                if (cost > 0) {
                    for (PopulationHouse.PopulationHouseBuild house : PopulationHouse.getHouses(e.unit.team())) {
                        if (house.population > 0) {
                            house.removePopulation(cost);
                            break;
                        }
                    }
                }
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
        SFWarPlanets.load();
        SFWarItems.load();
        //SFWarBlocks.load();
        TerranBlocks.load();
        NebulaeBlocks.load();
        SFWarDebugBlocks.load();
        Log.info("Ready for Action");
    }

}
