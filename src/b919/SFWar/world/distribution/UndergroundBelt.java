package b919.SFWar.world.distribution;

import arc.Events;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.g2d.Lines;
import arc.math.Mathf;
import arc.math.geom.Intersector;
import arc.math.geom.Vec2;
import arc.struct.Seq;
import arc.util.Time;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.gen.Building;
import mindustry.graphics.Drawf;
import mindustry.world.Tile;
import mindustry.world.blocks.distribution.ItemBridge;

import static mindustry.Vars.world;
// TODO todo the todo of todo
//  rewrite visuals
public class UndergroundBelt extends ItemBridge {
    public int depth = 1;
    public static Color[] depthColors = {Color.cyan, Color.gold, Color.magenta, Color.orange, Color.lime, Color.purple};
    public static Seq<UndergroundBeltBuild> allBelts = new Seq<>();
    private static final Vec2 isect = new Vec2();
    static {
        Events.on(EventType.WorldLoadBeginEvent.class, e -> {
            allBelts.clear();
        });
    }

    public UndergroundBelt(String name) {
        super(name);
        hasItems = true;
        hasPower = false;
        configurable = true;
        transportTime = 2f;
    }

    @Override
    public boolean outputsItems() {
        return true;
    }

    public Color getDepthColor() {
        return depthColors[Mathf.mod(depth - 1, depthColors.length)];
    }

    @Override
    public void drawPlace(int x, int y, int rotation, boolean valid) {
        drawUndergroundOverlay();

        Tile dest = findLink(x, y);
        if (dest != null && dest.build instanceof UndergroundBeltBuild other) {
            float x1 = x * Vars.tilesize + offset, y1 = y * Vars.tilesize + offset;
            float x2 = dest.worldx(), y2 = dest.worldy();
            boolean collision = checkCollision(x1, y1, x2, y2, depth, other);

            Draw.color(collision ? Color.red : getDepthColor());
            Lines.stroke(2f);
            Lines.line(x1, y1, x2, y2);

            drawArrow(x2, y2, x1, y1, collision ? Color.red : Color.white);

            if (collision) {
                drawCollisionCross(isect.x, isect.y);
            }
        }

        super.drawPlace(x, y, rotation, valid);
    }

    private static void drawCollisionCross(float x, float y) {
        Draw.color(Color.red);
        Lines.stroke(2f);
        float s = 5f;
        Lines.line(x - s, y - s, x + s, y + s);
        Lines.line(x - s, y + s, x + s, y - s);
        Draw.reset();
    }

    public void drawArrow(float x1, float y1, float x2, float y2, Color color) {
        float time = (Time.time / 40f) % 1f;
        float dx = x2 - x1, dy = y2 - y1;
        float len = Mathf.len(dx, dy);
        if (len < 1f) return;

        float px = x1 + dx * time;
        float py = y1 + dy * time;

        Draw.color(color);
        Drawf.tri(px, py, 4f, 6f, Mathf.angle(dx, dy));
        Draw.reset();
    }

    public boolean checkCollision(float x1, float y1, float x2, float y2, int depth, Building ignore) {
        float minx = Math.min(x1, x2), miny = Math.min(y1, y2), maxx = Math.max(x1, x2), maxy = Math.max(y1, y2);
        for (int i = 0; i < allBelts.size; i++) {
            UndergroundBeltBuild other = allBelts.get(i);
            if (other != ignore && ((UndergroundBelt) other.block).depth == depth) {
                Building t = other.target();
                if (t != null) {
                    float ominx = Math.min(other.x, t.x), ominy = Math.min(other.y, t.y), omaxx = Math.max(other.x, t.x), omaxy = Math.max(other.y, t.y);
                    if (maxx < ominx || minx > omaxx || maxy < ominy || miny > omaxy) continue;

                    if (isRealIntersection(x1, y1, x2, y2, other.x, other.y, t.x, t.y)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isRealIntersection(float x1, float y1, float x2, float y2, float x3, float y3, float x4, float y4) {
        if (Intersector.intersectSegments(x1, y1, x2, y2, x3, y3, x4, y4, isect)) {
            float eps = 0.1f;
            return !isect.within(x1, y1, eps) && !isect.within(x2, y2, eps) && !isect.within(x3, y3, eps) && !isect.within(x4, y4, eps);
        }
        return false;
    }

    private void drawUndergroundOverlay() {
        for (int i = 0; i < allBelts.size; i++) {
            UndergroundBeltBuild other = allBelts.get(i);
            Color c = ((UndergroundBelt) other.block).getDepthColor();
            Draw.color(c, 0.5f);
            Fill.square(other.x, other.y, other.block.size * Vars.tilesize / 2f - 1f);

            Building t = other.target();
            if (t != null) {
                Lines.stroke(1.5f);
                Lines.line(other.x, other.y, t.x, t.y);
            }
        }
        Draw.reset();
    }

    public class UndergroundBeltBuild extends ItemBridgeBuild {
        public Building target() {
            return Vars.world.build(link);
        }

        @Override
        public void add() {
            super.add();
            allBelts.add(this);
        }

        @Override
        public void remove() {
            super.remove();
            allBelts.remove(this);
        }

        @Override
        public void draw() {
            Draw.rect(block.region, x, y);
            drawCracks();
        }

        @Override
        public void drawSelect() {
            drawUndergroundOverlay();

            Building t = target();
            if (t != null) {
                boolean collision = checkCollision(x, y, t.x, t.y, depth, this);
                drawArrow(x, y, t.x, t.y, collision ? Color.red : Color.white);

                if (collision) {
                    drawCollisionCross(isect.x, isect.y);
                }
            }
        }

        @Override
        public void updateTile() {
            if (timer(timerCheckMoved, 30f)) {
                wasMoved = moved;
                moved = false;
            }
            timeSpeed = Mathf.approachDelta(timeSpeed, wasMoved ? 1f : 0f, 1f / 60f);
            time += timeSpeed * delta();
            checkIncoming();

            Tile other = world.tile(link);
            if (!linkValid(tile, other)) {
                doDump();
                warmup = 0f;
            } else {
                var inc = ((ItemBridgeBuild) other.build).incoming;
                int pos = tile.pos();
                if (!inc.contains(pos)) {
                    inc.add(pos);
                }
                warmup = Mathf.approachDelta(warmup, efficiency, 1f / 30f);

                Building collided = findCollisionTarget();
                if (collided != null) {
                    updateTransport(collided);
                } else {
                    updateTransport(other.build);
                }
            }
        }

        private Building findCollisionTarget() {
            Tile linkTile = world.tile(link);
            if (linkTile == null) return null;

            float x1 = x, y1 = y;
            float x2 = linkTile.worldx(), y2 = linkTile.worldy();

            for (int i = 0; i < allBelts.size; i++) {
                UndergroundBeltBuild other = allBelts.get(i);
                if (other == this || ((UndergroundBelt) other.block).depth != depth) continue;

                Building t = other.target();
                if (t != null && isRealIntersection(x1, y1, x2, y2, other.x, other.y, t.x, t.y)) {
                    return t;
                }
            }
            return null;
        }
    }
}
