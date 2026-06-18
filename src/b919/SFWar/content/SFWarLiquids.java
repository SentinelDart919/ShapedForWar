package b919.SFWar.content;

import arc.graphics.Color;
import mindustry.type.Liquid;

public class SFWarLiquids {
    public static Liquid
            blood,
            gaseousNyctar;
    public static void load(){
        blood = new Liquid("blood", Color.valueOf("000000")){{}};
        gaseousNyctar = new Liquid("gaseous-nyctar", Color.valueOf("000000")){{}};
    }
}

