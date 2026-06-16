package b919.SFWar.content;

import arc.func.*;
import arc.math.*;
import arc.math.geom.*;
import arc.math.geom.QuadTree.*;
import arc.struct.*;
import mindustry.*;
import mindustry.core.*;
import mindustry.entities.*;
import mindustry.game.*;
import mindustry.game.Teams.*;
import mindustry.gen.*;
import mindustry.world.*;

import java.util.*;

public class SFWarUtils{
    public static Rect r = new Rect(), r2 = new Rect();
    static Vec2 v2 = new Vec2(), v3 = new Vec2(), v4 = new Vec2();
    static IntSet collided = new IntSet(), collided2 = new IntSet();

    public static Vec2 v = new Vec2(), vv = new Vec2();
    public static Rand rand = new Rand(), rand2 = new Rand();

    public static Seq<Building> buildings = new Seq<>();

    public static float inRayCastCircle(float x, float y, float[] in, Sized target){
        float amount = 0f;
        float hsize = target.hitSize() / 2f;
        int collision = 0;
        int isize = in.length;

        float dst = Mathf.dst(x, y, target.getX(), target.getY());
        float ang = Angles.angle(x, y, target.getX(), target.getY());
        float angSize = Mathf.angle(dst, hsize);

        int idx1 = (int)(((ang - angSize) / 360f) * isize + 0.5f);
        int idx2 = (int)(((ang + angSize) / 360f) * isize + 0.5f);

        for(int i = idx1; i <= idx2; i++){
            int mi = Mathf.mod(i, isize);
            float range = in[mi];

            if((dst - hsize) < range){
                amount += Mathf.clamp((range - (dst - hsize)) / hsize);
            }
            collision++;
        }

        return collision > 0 ? (amount / collision) : 0f;
    }

    public static void rayCastCircle(float x, float y, float radius, Boolf<Tile> stop, Cons<Tile> ambient, Cons<Tile> edge, Cons<Building> hit, float[] out){
        Arrays.fill(out, radius);

        int res = out.length;
        collided.clear();
        collided2.clear();
        buildings.clear();
        for(int i = 0; i < res; i++){
            final int fi = i;
            float ang = (i / (float)res) * 360f;
            v2.trns(ang, radius).add(x, y);
            float vx = v2.x, vy = v2.y;
            int tx1 = (int)(x / Vars.tilesize), ty1 = (int)(y / Vars.tilesize);
            int tx2 = (int)(vx / Vars.tilesize), ty2 = (int)(vy / Vars.tilesize);

            World.raycastEach(tx1, ty1, tx2, ty2, (rx, ry) -> {
                Tile tile = Vars.world.tile(rx, ry);
                boolean collide = false;

                if(tile != null && !tile.block().isAir() && stop.get(tile)){
                    tile.getBounds(r2);
                    r2.grow(0.1f);
                    Vec2 inter = intersectRect(x, y, vx, vy, r2);
                    if(inter != null){
                        if(tile.build != null && collided.add(tile.build.id)){
                            buildings.add(tile.build);
                        }

                        float dst = Mathf.dst(x, y, inter.x, inter.y);
                        out[fi] = dst;
                        collide = true;
                    }else{
                        for(Point2 d : Geometry.d8){
                            Tile nt = Vars.world.tile(tile.x + d.x, tile.y + d.y);

                            if(nt != null && !nt.block().isAir() && stop.get(nt)){
                                nt.getBounds(r2);
                                r2.grow(0.1f);
                                Vec2 inter2 = intersectRect(x, y, vx, vy, r2);
                                if(inter2 != null){
                                    if(tile.build != null && collided.add(tile.build.id)){
                                        buildings.add(tile.build);
                                    }

                                    float dst = Mathf.dst(x, y, inter2.x, inter2.y);
                                    out[fi] = dst;
                                    collide = true;
                                }
                            }
                        }
                    }
                }

                if(tile != null && collided2.add(tile.pos())){
                    ambient.get(tile);
                    if(collide){
                        edge.get(tile);
                    }
                }

                return collide;
            });
        }
        for(Building b : buildings){
            hit.get(b);
        }
        buildings.clear();
    }

    public static void scanEnemies(Team team, float x, float y, float radius, boolean targetAir, boolean targetGround, Cons<Teamc> cons){
        r.setCentered(x, y, radius * 2f);
        Groups.unit.intersect(r.x, r.y, r.width, r.height, u -> {
            if(u.team != team && Mathf.within(x, y, u.x, u.y, radius + u.hitSize / 2f) && u.checkTarget(targetAir, targetGround)){
                cons.get(u);
            }
        });

        if(targetGround){
            buildings.clear();
            for(TeamData data : Vars.state.teams.active){
                if(data.team != team && data.buildingTree != null){
                    data.buildingTree.intersect(r, b -> {
                        if(Mathf.within(x, y, b.x, b.y, radius + b.hitSize() / 2f)){
                            buildings.add(b);
                        }
                    });
                }
            }
            for(Building b : buildings){
                cons.get(b);
            }

            buildings.clear();
        }
    }

    public static Vec2 intersectRect(float x1, float y1, float x2, float y2, Rect rect){
        boolean intersected = false;

        float nearX = 0f, nearY = 0f;
        float lastDst = 0f;

        for(int i = 0; i < 4; i++){
            int mod = i % 2;
            float rx1 = i < 2 ? (rect.x + rect.width * mod) : rect.x;
            float rx2 = i < 2 ? (rect.x + rect.width * mod) : rect.x + rect.width;
            float ry1 = i < 2 ? rect.y : (rect.y + rect.height * mod);
            float ry2 = i < 2 ? rect.y + rect.height : (rect.y + rect.height * mod);

            if(Intersector.intersectSegments(x1, y1, x2, y2, rx1, ry1, rx2, ry2, vv)){
                float dst = Mathf.dst2(x1, y1, vv.x, vv.y);
                if(!intersected || dst < lastDst){
                    nearX = vv.x;
                    nearY = vv.y;
                    lastDst = dst;
                }

                intersected = true;
            }
        }

        if(rect.contains(x1, y1)){
            nearX = x1;
            nearY = y1;
            intersected = true;
        }

        return intersected ? v2.set(nearX, nearY) : null;
    }
}
