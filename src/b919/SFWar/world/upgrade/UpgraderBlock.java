package b919.SFWar.world.upgrade;

import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.io.Reads;
import arc.util.io.Writes;
import b919.SFWar.ui.UpgraderDialog;
import mindustry.gen.Building;
import mindustry.gen.Icon;
import mindustry.ui.Styles;
import mindustry.world.Block;
/** Block that handles Updates and opens up the dialog*/
public class UpgraderBlock extends Block {
    public Seq<Upgrade> upgrades = new Seq<>();

    public UpgraderBlock(String name) {
        super(name);
        update = true;
        hasItems = true;
        solid = true;
        configurable = true;
        saveConfig = true;
    }

    public class UpgraderBuild extends Building {
        @Override
        public void buildConfiguration(Table table) {
            table.button(Icon.up, Styles.clearTogglei, () -> {
                new UpgraderDialog(this).show();
            }).size(50f);
        }

        @Override
        public void write(Writes write) {
            super.write(write);
            UpgradeManager.write(write);
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);
            UpgradeManager.read(read);
        }
    }
}
