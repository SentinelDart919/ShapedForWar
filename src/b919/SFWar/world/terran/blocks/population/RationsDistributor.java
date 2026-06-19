package b919.SFWar.world.terran.blocks.population;

import arc.graphics.g2d.*;
import arc.math.*;
import arc.struct.Seq;
import arc.util.Time;
import arc.util.io.*;
import mindustry.Vars;
import mindustry.gen.*;
import mindustry.graphics.*;
import mindustry.type.Item;
import mindustry.world.*;
import mindustry.world.consumers.*;
import mindustry.world.Tile;
/** Connects To Population Houses to distribute items / food items*/
public class RationsDistributor extends Block {
    //TODO add a radius draw to the radius that the Distributor works, add a way to manually connect houses
    public int maxConnections = 8;
    public float range = 80f;
    public float transferTime = 60f;
    public Item foodItem;

    public RationsDistributor(String name) {
        super(name);
        hasItems = true;
        hasPower = true;
        consumesPower = true;
        configurable = true;
        update = true;
        solid = true;
        sync = true;
        itemCapacity = 50;
        health = 400;
        consume(new ConsumePower(0.5f, 0f, false));
    }

    public class RationsDistributorBuild extends Building {
        public Seq<PopulationHouse.PopulationHouseBuild> connectedHouses = new Seq<>();
        public int transferIndex = 0;
        public float transferTimer = 0f;
        public float flashTimer = 0f;
        public float flashLerp = 0f;
        public Building flashTarget = null;

        @Override
        public void created() {
            super.created();
            autoConnect();
        }

        @Override
        public void onRemoved() {
            for (PopulationHouse.PopulationHouseBuild house : connectedHouses) {
                if (house.isAdded()) {
                    house.removeDistributor(this);
                }
            }
            connectedHouses.clear();
            super.onRemoved();
        }

        @Override
        public void updateTile() {
            super.updateTile();

            connectedHouses.removeAll(h -> h == null || !h.isAdded());

            if (connectedHouses.size < maxConnections && Time.time % 120f < Time.delta) {
                autoConnect();
            }

            if (flashTimer > 0f) {
                flashTimer -= Time.delta;
                flashLerp += Time.delta / 20f;
            }

            if (efficiency <= 0f || connectedHouses.size == 0) return;

            Item item = foodItem;
            if (item == null || !items.has(item)) return;

            transferTimer += Time.delta;
            if (transferTimer >= transferTime) {
                transferTimer = 0f;
                doTransfer(item);
            }
        }

        @Override
        public boolean acceptItem(Building source, Item item) {
            return item == foodItem && items.get(item) < itemCapacity;
        }

        private void doTransfer(Item item) {
            for (int i = 0; i < connectedHouses.size; i++) {
                int idx = (transferIndex + i) % connectedHouses.size;
                PopulationHouse.PopulationHouseBuild house = connectedHouses.get(idx);
                if (house != null && house.isAdded() &&
                    house.items.get(item) < house.block.itemCapacity) {
                    items.remove(item, 1);
                    house.distributorDeliver(item, 1);
                    transferIndex = (idx + 1) % connectedHouses.size;
                    flashTimer = 20f;
                    flashLerp = 0f;
                    flashTarget = house;
                    break;
                }
            }
        }

        private void autoConnect() {
            int rad = (int)(range / Vars.tilesize) + 1;
            int tx = tile.x, ty = tile.y;

            for (int dx = -rad; dx <= rad && connectedHouses.size < maxConnections; dx++) {
                for (int dy = -rad; dy <= rad && connectedHouses.size < maxConnections; dy++) {
                    Tile t = Vars.world.tile(tx + dx, ty + dy);
                    if (t == null || !(t.build instanceof PopulationHouse.PopulationHouseBuild house)) continue;
                    if (connectedHouses.contains(house)) continue;
                    if (house.distributors.size > 0) continue;
                    if (Mathf.dst(t.worldx(), t.worldy(), x, y) > range) continue;

                    connectedHouses.add(house);
                    house.addDistributor(this);
                }
            }
        }

        private boolean toggleConnection(PopulationHouse.PopulationHouseBuild house) {
            if (connectedHouses.contains(house)) {
                connectedHouses.remove(house);
                house.removeDistributor(this);
                return false;
            } else if (connectedHouses.size < maxConnections) {
                connectedHouses.add(house);
                house.addDistributor(this);
                return true;
            }
            return false;
        }

        @Override
        public void draw() {
            super.draw();

            Draw.color(Pal.accent);
            Lines.stroke(1.5f);

            for (PopulationHouse.PopulationHouseBuild house : connectedHouses) {
                if (!house.isAdded()) continue;
                float hx = house.x, hy = house.y;
                Lines.line(x, y, hx, hy);
                Lines.square(hx, hy, 2f, 45f);
            }

            if (flashTimer > 0f && flashTarget != null && flashTarget.isAdded()) {
                float progress = Mathf.clamp(flashLerp);
                Draw.color(Pal.lighterOrange);
                Lines.stroke(2f);
                float mx = Mathf.lerp(x, flashTarget.x, progress);
                float my = Mathf.lerp(y, flashTarget.y, progress);
                Lines.line(x, y, mx, my);
                Fill.circle(mx, my, 3f);
                Lines.stroke(1f);
            }

            Draw.reset();
        }

        @Override
        public void write(Writes write) {
            super.write(write);
            write.s(connectedHouses.size);
            for (PopulationHouse.PopulationHouseBuild house : connectedHouses) {
                write.i(house.pos());
            }
        }

        @Override
        public void read(Reads read, byte revision) {
            super.read(read, revision);
            short count = read.s();
            connectedHouses.clear();
            for (int i = 0; i < count; i++) {
                int pos = read.i();
                Building build = Vars.world.build(pos);
                if (build instanceof PopulationHouse.PopulationHouseBuild house) {
                    connectedHouses.add(house);
                    house.addDistributor(this);
                }
            }
        }
    }
}
