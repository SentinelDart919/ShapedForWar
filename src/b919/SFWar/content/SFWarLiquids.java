package b919.SFWar.content;

import arc.graphics.Color;
import mindustry.type.Liquid;

public class SFWarLiquids {
    public static Liquid
            blood,
            liquidNyctar ,gaseousNyctar;
    public static void load(){
        blood = new Liquid("blood", Color.valueOf("000000")){{}};
        liquidNyctar = new Liquid("liquid-nyctar", Color.valueOf("7d00ab")){{

        }};
        gaseousNyctar = new Liquid("gaseous-nyctar", Color.valueOf("ab1ae0")){{
            gas = true;
        }};
    }
}

