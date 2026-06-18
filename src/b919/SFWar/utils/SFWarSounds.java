package b919.SFWar.utils;

import arc.audio.*;
import mindustry.*;

public class SFWarSounds{
    public static Sound desNukeShoot, desNukeHit, desNukeHitFar;

    public static void load(){
        desNukeShoot = Vars.tree.loadSound("des-nuke-shoot");
        desNukeHit = Vars.tree.loadSound("des-nuke-hit");
        desNukeHitFar = Vars.tree.loadSound("des-nuke-hit-far");
    }
}
