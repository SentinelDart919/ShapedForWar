package b919.SFWar.content;

import mindustry.content.Items;
import mindustry.world.Block;
import mindustry.world.blocks.environment.Floor;
import mindustry.world.blocks.environment.OreBlock;

public class SFWarBlocks {
    public static Block
    oreFerrum, oreUranium, oreChromium, sulfurFlats;
    public static void load(){
        oreFerrum = new OreBlock("ferrum-ore"){{
            itemDrop = SFWarItems.ferrum;
            oreDefault = true;
            oreThreshold = 0.852f;
            oreScale = 25.380253f;
        }};
        oreUranium = new OreBlock("uranium-ore"){{
            itemDrop = SFWarItems.uraninite;
            oreDefault = true;
            oreThreshold = 0.812f;
            oreScale = 23.17619f;
        }};
        oreChromium = new OreBlock("chromium-ore"){{
            itemDrop = SFWarItems.chromium;
            oreDefault = true;
        }};
        sulfurFlats = new Floor("sulfurFlats"){{
            itemDrop = SFWarItems.sulfur;
            playerUnmineable = true;
        }};
    }
}
