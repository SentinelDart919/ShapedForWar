package b919.SFWar.ui;

import arc.Core;
import arc.graphics.*;
import arc.graphics.g2d.*;
import arc.input.*;
import arc.math.*;
import arc.math.geom.*;
import arc.scene.*;
import arc.scene.event.*;
import arc.scene.ui.*;
import arc.scene.ui.layout.*;
import arc.struct.*;
import arc.util.*;
import b919.SFWar.world.upgrade.*;
import mindustry.Vars;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.ItemStack;
import mindustry.ui.*;
import mindustry.ui.dialogs.BaseDialog;
import mindustry.world.Block;

public class UpgraderDialog extends BaseDialog {
    private static final float NODE_SIZE = 60f;
    private static final float H_GAP = 20f;
    private static final float V_GAP = 40f;
    private static final float PADDING = 40f;

    private final UpgraderBlock.UpgraderBuild build;
    public View view;

    public UpgraderDialog(UpgraderBlock.UpgraderBuild build) {
        super("Upgrades");
        this.build = build;

        titleTable.remove();
        margin(0f).marginBottom(8);
        cont.add(view = new View()).grow();
        addCloseButton();

        addListener(new InputListener(){
            @Override
            public boolean scrolled(InputEvent event, float x, float y, float amountX, float amountY){
                view.setScale(Mathf.clamp(view.scaleX - amountY / 10f * view.scaleX, 0.25f, 1f));
                view.setOrigin(Align.center);
                view.setTransform(true);
                return true;
            }

            @Override
            public boolean mouseMoved(InputEvent event, float x, float y){
                view.requestScroll();
                return super.mouseMoved(event, x, y);
            }
        });

        touchable = Touchable.enabled;

        addCaptureListener(new ElementGestureListener(){
            @Override
            public void zoom(InputEvent event, float initialDistance, float distance){
                if(view.lastZoom < 0){
                    view.lastZoom = view.scaleX;
                }
                view.setScale(Mathf.clamp(distance / initialDistance * view.lastZoom, 0.25f, 1f));
                view.setOrigin(Align.center);
                view.setTransform(true);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, KeyCode button){
                view.lastZoom = view.scaleX;
            }

            @Override
            public void pan(InputEvent event, float x, float y, float deltaX, float deltaY){
                view.panX += deltaX / view.scaleX;
                view.panY += deltaY / view.scaleY;
                view.moved = true;
                view.clamp();
            }
        });
    }

    private boolean canUnlock(Upgrade upgrade) {
        int currentTier = UpgradeManager.getTier(upgrade.name);
        if(currentTier >= upgrade.tiers.size) return false;

        for(Upgrade prereq : upgrade.prerequisites) {
            if(UpgradeManager.getTier(prereq.name) <= 0) return false;
        }

        UpgradeTier nextTier = upgrade.tiers.get(currentTier);

        for(Block block : nextTier.needs) {
            if(!isBlockBuilt(block)) return false;
        }

        if(Vars.player == null || Vars.player.core() == null) return false;
        for(ItemStack stack : nextTier.requirements) {
            if(Vars.player.core().items.get(stack.item) < stack.amount) return false;
        }

        return true;
    }

    private void unlockUpgrade(Upgrade upgrade) {
        int currentTier = UpgradeManager.getTier(upgrade.name);
        UpgradeTier nextTier = upgrade.tiers.get(currentTier);

        for(ItemStack stack : nextTier.requirements) {
            Vars.player.core().items.remove(stack.item, stack.amount);
        }

        UpgradeManager.setTier(upgrade.name, currentTier + 1);

        hide();
        new UpgraderDialog(build).show();
    }

    private boolean isBlockBuilt(Block block) {
        for(Building b : Groups.build) {
            if(b.block == block && b.team == Vars.player.team()) return true;
        }
        return false;
    }

    public class View extends Group {
        public float panX = 0, panY = 0, lastZoom = -1;
        public boolean moved = false;
        public @Nullable Upgrade hoverUpgrade;
        public Table infoTable = new Table();

        private final Seq<UpgradeNode> nodes = new Seq<>();
        private final ObjectMap<Upgrade, UpgradeNode> nodeMap = new ObjectMap<>();
        private Rect bounds = new Rect();

        {
            rebuild();
        }

        public void rebuild(){
            clear();
            nodes.clear();
            nodeMap.clear();
            hoverUpgrade = null;
            infoTable.clear();
            infoTable.touchable = Touchable.enabled;

            Seq<Upgrade> upgrades = ((UpgraderBlock) build.block).upgrades;
            if(upgrades.isEmpty()){
                Label empty = new Label("No upgrades available");
                addChild(empty);
                return;
            }

            ObjectSet<Upgrade> upgradeSet = new ObjectSet<>();
            upgradeSet.addAll(upgrades);

            ObjectSet<Upgrade> dependents = new ObjectSet<>();
            for(Upgrade u : upgrades){
                for(Upgrade pre : u.prerequisites){
                    if(upgradeSet.contains(pre)){
                        dependents.add(pre);
                    }
                }
            }

            Seq<Upgrade> connected = new Seq<>();
            Seq<Upgrade> standalone = new Seq<>();
            for(Upgrade u : upgrades){
                boolean hasPrereqInList = false;
                for(Upgrade pre : u.prerequisites){
                    if(upgradeSet.contains(pre)){
                        hasPrereqInList = true;
                        break;
                    }
                }
                if(hasPrereqInList || dependents.contains(u)){
                    connected.add(u);
                }else{
                    standalone.add(u);
                }
            }

            ObjectSet<Upgrade> visited = new ObjectSet<>();
            ObjectMap<Upgrade, Integer> depths = new ObjectMap<>();
            Seq<Upgrade> roots = new Seq<>();

            for(Upgrade u : connected){
                boolean hasValidPrereq = false;
                for(Upgrade pre : u.prerequisites){
                    if(connected.contains(pre)){
                        hasValidPrereq = true;
                        break;
                    }
                }
                if(!hasValidPrereq){
                    roots.add(u);
                    depths.put(u, 0);
                    visited.add(u);
                }
            }

            boolean changed = true;
            while(changed){
                changed = false;
                for(Upgrade u : connected){
                    if(visited.contains(u)) continue;

                    boolean allPrereqsAssigned = true;
                    int maxPrereqDepth = 0;
                    boolean hasAnyPrereq = false;

                    for(Upgrade pre : u.prerequisites){
                        if(!connected.contains(pre)) continue;
                        hasAnyPrereq = true;
                        if(!visited.contains(pre)){
                            allPrereqsAssigned = false;
                            break;
                        }
                        maxPrereqDepth = Math.max(maxPrereqDepth, depths.get(pre, 0));
                    }

                    if(!hasAnyPrereq){
                        roots.add(u);
                        depths.put(u, 0);
                        visited.add(u);
                        changed = true;
                    }else if(allPrereqsAssigned){
                        depths.put(u, maxPrereqDepth + 1);
                        visited.add(u);
                        changed = true;
                    }
                }
            }

            IntMap<Seq<Upgrade>> levels = new IntMap<>();
            int maxDepth = 0;
            for(var entry : depths){
                levels.get(entry.value, Seq::new).add(entry.key);
                maxDepth = Math.max(maxDepth, entry.value);
            }

            int maxLevelSize = 0;
            for(int d = 0; d <= maxDepth; d++){
                Seq<Upgrade> level = levels.get(d, Seq::new);
                maxLevelSize = Math.max(maxLevelSize, level.size);
            }

            float treeWidth = Math.max(maxLevelSize, standalone.size) * (NODE_SIZE + H_GAP) - H_GAP;
            float treeHeight = (maxDepth + 1) * (NODE_SIZE + V_GAP) - V_GAP;
            if(treeWidth < NODE_SIZE) treeWidth = NODE_SIZE;
            if(treeHeight < NODE_SIZE) treeHeight = NODE_SIZE;

            for(int d = 0; d <= maxDepth; d++){
                Seq<Upgrade> level = levels.get(d, Seq::new);
                float levelWidth = level.size * (NODE_SIZE + H_GAP) - H_GAP;
                float startX = (treeWidth - levelWidth) / 2f;

                for(int i = 0; i < level.size; i++){
                    Upgrade upgrade = level.get(i);
                    UpgradeNode node = new UpgradeNode(upgrade, d);
                    node.x = startX + i * (NODE_SIZE + H_GAP) + NODE_SIZE / 2f;
                    node.y = treeHeight - (d + 1) * (NODE_SIZE + V_GAP) + V_GAP + NODE_SIZE / 2f;

                    nodes.add(node);
                    nodeMap.put(upgrade, node);
                    addButton(node);
                }
            }

            if(!standalone.isEmpty()){
                float standaloneY = -NODE_SIZE - V_GAP;
                float standaloneWidth = standalone.size * (NODE_SIZE + H_GAP) - H_GAP;
                float standaloneStartX = (treeWidth - standaloneWidth) / 2f;

                for(int i = 0; i < standalone.size; i++){
                    Upgrade upgrade = standalone.get(i);
                    UpgradeNode node = new UpgradeNode(upgrade, -1);
                    node.x = standaloneStartX + i * (NODE_SIZE + H_GAP) + NODE_SIZE / 2f;
                    node.y = standaloneY;

                    nodes.add(node);
                    nodeMap.put(upgrade, node);
                    addButton(node);
                }

                treeHeight += NODE_SIZE + V_GAP;
            }

            float minx = 0f, miny = 0f, maxx = 0f, maxy = 0f;
            for(UpgradeNode n : nodes){
                minx = Math.min(n.x - NODE_SIZE / 2f, minx);
                maxx = Math.max(n.x + NODE_SIZE / 2f, maxx);
                miny = Math.min(n.y - NODE_SIZE / 2f, miny);
                maxy = Math.max(n.y + NODE_SIZE / 2f, maxy);
            }
            bounds = new Rect(minx, miny, maxx - minx, maxy - miny);

            setOrigin(Align.center);
            setTransform(true);
            released(() -> moved = false);
        }

        private void addButton(UpgradeNode node){
            Upgrade upgrade = node.upgrade;
            int currentTier = UpgradeManager.getTier(upgrade.name);
            boolean maxTier = currentTier >= upgrade.tiers.size;
            boolean canUnlock = canUnlock(upgrade);

            ImageButton button = new ImageButton(upgrade.getDisplayIcon(), Styles.nodei);
            button.resizeImage(32f);
            button.getImage().setScaling(Scaling.fit);

            if(maxTier){
                button.getStyle().imageUpColor = Color.green;
            }else if(canUnlock){
                button.getStyle().imageUpColor = Color.yellow;
            }else if(currentTier > 0){
                button.getStyle().imageUpColor = Color.orange;
            }else{
                button.getStyle().imageUpColor = Color.gray;
            }

            button.clicked(() -> {
                if(moved) return;
                if(hoverUpgrade == upgrade){
                    hoverUpgrade = null;
                }else{
                    hoverUpgrade = upgrade;
                }
                rebuildInfo();
            });

            button.hovered(() -> {
                if(hoverUpgrade != upgrade){
                    hoverUpgrade = upgrade;
                    rebuildInfo();
                }
            });
            button.exited(() -> {
                if(hoverUpgrade == upgrade && !infoTable.hasMouse() && !button.hasMouse()){
                    hoverUpgrade = null;
                    rebuildInfo();
                }
            });

            button.setSize(NODE_SIZE);
            button.update(() -> {
                button.setPosition(node.x + panX + width / 2f, node.y + panY + height / 2f, Align.center);
            });

            button.userObject = upgrade;
            addChild(button);
        }

        void clamp(){
            float pad = NODE_SIZE;
            float ox = width / 2f, oy = height / 2f;
            float rx = bounds.x + panX + ox, ry = panY + oy + bounds.y;
            float rw = bounds.width, rh = bounds.height;
            rx = Mathf.clamp(rx, -rw + pad, Core.graphics.getWidth() - pad);
            ry = Mathf.clamp(ry, -rh + pad, Core.graphics.getHeight() - pad);
            panX = rx - bounds.x - ox;
            panY = ry - bounds.y - oy;
        }

        void rebuildInfo(){
            infoTable.remove();
            infoTable.clear();
            infoTable.update(null);
            infoTable.touchable = Touchable.enabled;

            if(hoverUpgrade == null) return;

            Upgrade upgrade = hoverUpgrade;
            int tier = UpgradeManager.getTier(upgrade.name);

            infoTable.update(() -> {
                UpgradeNode n = nodeMap.get(upgrade);
                if(n != null){
                    infoTable.setPosition(n.x + panX + width / 2f + NODE_SIZE / 2f, n.y + panY + height / 2f + NODE_SIZE / 2f, Align.topLeft);
                }
            });

            infoTable.left();
            infoTable.background(Tex.button).margin(8f);

            infoTable.table(desc -> {
                desc.left().defaults().left();
                desc.add("[white]" + upgrade.name + "[]").color(Pal.accent);
                desc.row();
                desc.add("[lightgray]" + upgrade.description + "[]").color(Color.lightGray);
                desc.row();
                desc.add("[accent]Level: " + tier + " / " + upgrade.tiers.size + "[]");
                desc.row();

                if(tier < upgrade.tiers.size){
                    UpgradeTier nextTier = upgrade.tiers.get(tier);

                    if(nextTier.requirements.length > 0){
                        for(int i = 0; i < nextTier.requirements.length; i++){
                            ItemStack s = nextTier.requirements[i];
                            boolean has = Vars.player != null && Vars.player.core() != null
                                && Vars.player.core().items.get(s.item) >= s.amount;

                            desc.table(req -> {
                                req.left();
                                req.image(s.item.uiIcon).size(8 * 3).padRight(3);
                                req.add(s.item.localizedName).color(Color.lightGray);
                                req.add(" " + Vars.player.core().items.get(s.item) + " / " + s.amount)
                                    .color(has ? Color.lightGray : Color.scarlet);
                            }).fillX().left();
                            desc.row();
                        }
                    }

                    if(nextTier.needs != null && !nextTier.needs.isEmpty()){
                        for(int i = 0; i < nextTier.needs.size; i++){
                            Block block = nextTier.needs.get(i);
                            boolean built = isBlockBuilt(block);
                            desc.table(req -> {
                                req.left();
                                req.add("> " + block.localizedName).color(Color.lightGray);
                                req.image((arc.scene.style.Drawable)(built ? Icon.ok : Icon.cancel)).color(built ? Color.lightGray : Color.scarlet).padLeft(3);
                            }).fillX().left();
                            desc.row();
                        }
                    }

                    if(nextTier.description != null && !nextTier.description.isEmpty()){
                        desc.add("[lightgray]" + nextTier.description + "[]").color(Color.lightGray);
                        desc.row();
                    }

                    boolean canUnlock = canUnlock(upgrade);
                    desc.button(canUnlock ? "[green]Unlock[]" : "[red]Locked[]", () -> {
                        if(canUnlock(upgrade)){
                            unlockUpgrade(upgrade);
                        }
                    }).size(120f, 35f).pad(4f);
                }else{
                    desc.add("[green]MAX LEVEL[]");
                }
            }).growX().left();

            addChild(infoTable);
            infoTable.pack();
            infoTable.act(Core.graphics.getDeltaTime());
        }

        @Override
        public void drawChildren(){
            clamp();
            float offsetX = panX + width / 2f, offsetY = panY + height / 2f;

            Draw.sort(true);

            for(UpgradeNode node : nodes){
                for(Upgrade prereq : node.upgrade.prerequisites){
                    UpgradeNode parent = nodeMap.get(prereq);
                    if(parent != null && node.depth != -1 && parent.depth != -1){
                        Draw.z(2f);
                        Lines.stroke(Scl.scl(3f), Pal.accent);
                        Draw.alpha(parentAlpha);
                        Lines.line(parent.x + offsetX, parent.y + offsetY, node.x + offsetX, node.y + offsetY);
                    }
                }
            }

            Draw.sort(false);
            Draw.reset();
            super.drawChildren();
        }
    }

    private static class UpgradeNode {
        Upgrade upgrade;
        int depth;
        float x, y;

        UpgradeNode(Upgrade upgrade, int depth){
            this.upgrade = upgrade;
            this.depth = depth;
        }
    }
}
