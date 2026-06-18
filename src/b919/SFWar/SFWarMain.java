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
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.mod.*;

public class SFWarMain extends Mod{

    public SFWarMain(){
        Log.info("Shaped For War loaded.");
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
        Log.info("Loading content.");
        SFWarPlanets.load();
        SFWarItems.load();
        //SFWarBlocks.load();
        TerranBlocks.load();
        NebulaeBlocks.load();
        SFWarDebugBlocks.load();
    }

}
