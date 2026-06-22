package b919.SFWar.content;

import arc.graphics.Color;
import mindustry.content.StatusEffects;
import mindustry.type.Liquid;

public class SFWarLiquids {
    public static Liquid
            blood,
            liquidNyctar ,gaseousNyctar;
    public static void load(){
        blood = new Liquid("blood", Color.valueOf("000000")){{}};
        liquidNyctar = new Liquid("liquid-nyctar", Color.valueOf("7d00ab")){{
            temperature = 0.1f;
            flammability = 0;
            heatCapacity = 0.7f;
            explosiveness = 0.4f;
            viscosity = 0.4f;
            boilPoint = 4f;
            blockReactive = false;
            coolant = true;
            moveThroughBlocks = false;
            incinerable = false;
            effect = StatusEffects.shocked;
        }};
        gaseousNyctar = new Liquid("gaseous-nyctar", Color.valueOf("ab1ae0")){{
            gas = true;
            temperature = 0.3f;
            flammability = 0;
            heatCapacity = 0.2f;
            explosiveness = 0.2f;
            viscosity = 0.1f;
            blockReactive = false;
            coolant = true;
            moveThroughBlocks = false;
            incinerable = false;
        }};
    }
}

