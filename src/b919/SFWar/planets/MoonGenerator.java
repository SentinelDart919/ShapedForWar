package b919.SFWar.planets;

import arc.graphics.Color;
import arc.math.Angles;
import arc.math.Mathf;
import arc.math.Rand;
import arc.math.geom.Geometry;
import arc.math.geom.Vec2;
import arc.math.geom.Vec3;
import arc.struct.*;
import arc.util.Tmp;
import arc.util.noise.Ridged;
import arc.util.noise.Simplex;
import mindustry.ai.Astar;
import mindustry.content.Blocks;
import mindustry.game.Schematics;
import mindustry.game.Waves;
import mindustry.maps.generators.PlanetGenerator;
import mindustry.world.Block;
import mindustry.world.Tile;
import mindustry.world.TileGen;

import static mindustry.Vars.*;

public class MoonGenerator extends PlanetGenerator {
    float scl = 7;
    float heightYOffset = 42.7f;
    float waterOffset = 0.02f;
    public float getSizeScl(){
        return 6500;
    }

    Block[][] mapfloor = {
        {Blocks.stone, Blocks.stone, Blocks.stone, Blocks.basalt, Blocks.darksand},
        {Blocks.stone, Blocks.stone, Blocks.basalt, Blocks.darksand, Blocks.darksand},
        {Blocks.darksand, Blocks.stone, Blocks.stone, Blocks.basalt, Blocks.basalt},
        {Blocks.basalt, Blocks.darksand, Blocks.stone, Blocks.stone, Blocks.stone},
        {Blocks.basalt, Blocks.basalt, Blocks.stone, Blocks.stone, Blocks.darksand},
    };

    ObjectMap<Block, Block> dec = ObjectMap.of(
        Blocks.stone, Blocks.boulder,
        Blocks.basalt, Blocks.basaltBoulder,
        Blocks.darksand, Blocks.basaltBoulder
    );

    float rawHeight(Vec3 position){
        float height = (Mathf.pow(Simplex.noise3d(seed, 7, 0.5f, 1f/3f, position.x * scl, position.y * scl + heightYOffset, position.z * scl), 2.3f) + waterOffset) / (1f + waterOffset);

        float crater = Ridged.noise3d(seed + 5, position.x * 3, position.y * 3, position.z * 3, 3, 20);
        if(crater > 0.4f){
            float strength = (crater - 0.4f) * 1.5f;
            height += strength * 0.06f;
            height -= strength * 0.12f;
        }

        return Mathf.clamp(height);
    }

    @Override
    public float getHeight(Vec3 position){
        return rawHeight(position);
    }

    @Override
    public void getColor(Vec3 position, Color out){
        Block block = getBlock(position, true);
        out.set(block.mapColor).a(1f - block.albedo);
    }

    @Override
    public void genTile(Vec3 position, TileGen tile){
        tile.floor = getBlock(position, false);
        tile.block = tile.floor.asFloor().wall;
        if(tile.floor.asFloor().isLiquid) tile.block = Blocks.air;
    }

    Block getBlock(Vec3 position, boolean visualOnly){
        float height = rawHeight(position);
        float px = position.x * scl, py = position.y * scl + heightYOffset, pz = position.z * scl;

        float rad = scl;
        float temp = Mathf.clamp(Math.abs(py * 2f) / (rad));
        float tnoise = Simplex.noise3d(seed, 7, 0.56, 1f/3f, px, py + 999f, pz);
        temp = Mathf.lerp(temp, tnoise, 0.5f);
        height *= 1.2f;
        height = Mathf.clamp(height);

        return mapfloor[Mathf.clamp((int)(temp * mapfloor.length), 0, mapfloor[0].length - 1)][Mathf.clamp((int)(height * mapfloor[0].length), 0, mapfloor[0].length - 1)];
    }

    @Override
    protected float noise(float x, float y, double octaves, double falloff, double scl, double mag){
        Vec3 v = sector.rect.project(x, y).scl(5f);
        return Simplex.noise3d(seed, octaves, falloff, 1f / scl, v.x, v.y, v.z) * (float)mag;
    }

    @Override
    protected void generate(){
        class Room{
            int x, y, radius;
            ObjectSet<Room> connected = new ObjectSet<>();
            Room(int x, int y, int radius){
                this.x = x;
                this.y = y;
                this.radius = radius;
                connected.add(this);
            }
            void join(int x1, int y1, int x2, int y2){
                float nscl = rand.random(100f, 140f) * 6f;
                int stroke = rand.random(3, 9);
                brush(pathfind(x1, y1, x2, y2, tile -> (tile.solid() ? 50f : 0f) + noise(tile.x, tile.y, 2, 0.4f, 1f / nscl) * 500, Astar.manhattan), stroke);
            }
            void connect(Room to){
                if(!connected.add(to) || to == this) return;

                Vec2 midpoint = Tmp.v1.set(to.x, to.y).add(x, y).scl(0.5f);
                rand.nextFloat();
                midpoint.add(Tmp.v2.setToRandomDirection(rand).scl(Tmp.v1.dst(x, y)));
                midpoint.sub(width/2f, height/2f).limit(width / 2f / Mathf.sqrt3).add(width/2f, height/2f);

                int mx = (int)midpoint.x, my = (int)midpoint.y;
                join(x, y, mx, my);
                join(mx, my, to.x, to.y);
            }
        }

        cells(4);
        distort(10f, 12f);

        float constraint = 1.3f;
        float radius = width / 2f / Mathf.sqrt3;
        int rooms = rand.random(2, 4);
        Seq<Room> roomseq = new Seq<>();

        for(int i = 0; i < rooms; i++){
            Tmp.v1.trns(rand.random(360f), rand.random(radius / constraint));
            float rx = (width/2f + Tmp.v1.x);
            float ry = (height/2f + Tmp.v1.y);
            float maxrad = radius - Tmp.v1.len();
            float rrad = Math.min(rand.random(9f, maxrad / 2f), 30f);
            roomseq.add(new Room((int)rx, (int)ry, (int)rrad));
        }

        Room spawn = null;
        Seq<Room> enemies = new Seq<>();
        int enemySpawns = rand.random(1, Math.max((int)(sector.threat * 4), 1));
        int offset = rand.nextInt(360);
        float length = width/2.55f - rand.random(13, 23);
        int angleStep = 5;

        for(int i = 0; i < 360; i += angleStep){
            int angle = offset + i;
            int cx = (int)(width/2 + Angles.trnsx(angle, length));
            int cy = (int)(height/2 + Angles.trnsy(angle, length));

            roomseq.add(spawn = new Room(cx, cy, rand.random(8, 15)));

            for(int j = 0; j < enemySpawns; j++){
                float enemyOffset = rand.range(60f);
                Tmp.v1.set(cx - width/2, cy - height/2).rotate(180f + enemyOffset).add(width/2, height/2);
                Room espawn = new Room((int)Tmp.v1.x, (int)Tmp.v1.y, rand.random(8, 16));
                roomseq.add(espawn);
                enemies.add(espawn);
            }

            break;
        }

        for(Room room : roomseq){
            erase(room.x, room.y, room.radius);
        }

        int connections = rand.random(Math.max(rooms - 1, 1), rooms + 3);
        for(int i = 0; i < connections; i++){
            roomseq.random(rand).connect(roomseq.random(rand));
        }

        for(Room room : roomseq){
            spawn.connect(room);
        }

        cells(1);
        distort(10f, 6f);

        Room finalSpawn = spawn;
        pass((x, y) -> {
            if(block.solid) return;

            float crater = Ridged.noise3d(seed + 10, x / 25f, y / 25f, 0, 3, 20);
            if(crater > 0.38f && !Mathf.within(x, y, finalSpawn.x, finalSpawn.y, 15)){
                floor = Blocks.stone;
            }
        });

        pass((x, y) -> {
            dec: {
                for(int i = 0; i < 4; i++){
                    Tile near = tiles.get(x + Geometry.d4[i].x, y + Geometry.d4[i].y);
                    if(near != null && near.block() != Blocks.air){
                        break dec;
                    }
                }

                if(rand.chance(0.012) && floor.asFloor().hasSurface() && block == Blocks.air){
                    block = dec.get(floor, floor.asFloor().decoration);
                }
            }
        });

        trimDark();
        median(2);
        inverseFloodFill(tiles.getn(spawn.x, spawn.y));
        tech();

        for(Tile tile : tiles){
            if(tile.overlay().needsSurface && !tile.floor().hasSurface()){
                tile.setOverlay(Blocks.air);
            }
        }

        Schematics.placeLaunchLoadout(spawn.x, spawn.y);

        for(Room espawn : enemies){
            tiles.getn(espawn.x, espawn.y).setOverlay(Blocks.spawn);
        }

        state.rules.winWave = sector.info.winWave = 10 + 5 * (int)Math.max(sector.threat * 10, 1);

        float waveTimeDec = 0.4f;
        state.rules.waveSpacing = Mathf.lerp(60 * 65 * 2, 60f * 60f * 1f, Math.max(sector.threat - waveTimeDec, 0f));
        state.rules.waves = true;
        state.rules.env = sector.planet.defaultEnv;
        state.rules.enemyCoreBuildRadius = 600f;
        state.rules.spawns = Waves.generate(sector.threat, new Rand(sector.id), state.rules.attackMode, state.rules.attackMode && spawner.countGroundSpawns() == 0);
    }
}
