package game.core.entities.enemies;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import game.core.entities.OwnerType;
import game.core.entities.Projectile;
import lib.GameLib;

// THE PATROLLER (NEM SEI SE ESSA PALAVRA EXISTE)

// BossType1 is a boss that patrols side to side within its arena, bouncing
// between a fixed left/right range, and fires a 3-way fan of bigger, harder
// hitting shots straight down at a (slower) fixed interval, ignoring the
// player's position entirely.
public class BossType1 extends Boss {

    private static final double SIZE = 60;
    private static final Color COLOR = Color.MAGENTA;

    // slower rate of fire than a single-shot boss would use, to balance out the extra shots/damage below
    private static final double SHOOT_INTERVAL_MS = 1600;
    private static final int SHOOT_DAMAGE = 2;
    private static final int CONTACT_DAMAGE = 1;
    private static final double PROJECTILE_SPEED = 320;
    private static final double PROJECTILE_WIDTH = 12;
    private static final double PROJECTILE_HEIGHT = 18;
    private static final double FAN_SPREAD_RADIANS = Math.PI / 10;

    private static final double ENTRY_SPEED = 150;

    private static final double PATROL_HALF_RANGE = 150;
    private static final double PATROL_SPEED = 120;

    private int direction = 1;

    public BossType1(double arenaX, double arenaY, int maxHealth) {
        // he starts off the screen and goes down vertical(we can change that)
        super(arenaX, -SIZE, SIZE, SIZE, maxHealth, COLOR, SHOOT_INTERVAL_MS, CONTACT_DAMAGE, SHOOT_DAMAGE, arenaX, arenaY, ENTRY_SPEED);
    }

    @Override
    protected void updateArenaMovement(long delta) {
        double seconds = delta / 1000.0;
        x += direction * PATROL_SPEED * seconds;

        double left = getArenaX() - PATROL_HALF_RANGE;
        double right = getArenaX() + PATROL_HALF_RANGE;

        if (x <= left) {
            x = left;
            direction = 1;
        } else if (x >= right) {
            x = right;
            direction = -1;
        }

        y = getArenaY();
    }

    @Override
    public List<Projectile> tryShoot(long currentTime, double targetX, double targetY) {
        if (!readyToShoot()) {
            return null;
        }

        double[] angles = { Math.PI/2 - FAN_SPREAD_RADIANS, Math.PI/2, Math.PI/2 + FAN_SPREAD_RADIANS };
        List<Projectile> shots = new ArrayList<>(angles.length);

        for (double angle : angles) {
            double vx = Math.cos(angle) * PROJECTILE_SPEED;
            double vy = Math.sin(angle) * PROJECTILE_SPEED;
            shots.add(makeProjectile(x, y + height / 2, vx, vy, OwnerType.ENEMY, Color.MAGENTA, PROJECTILE_WIDTH, PROJECTILE_HEIGHT));
        }

        return shots;
    }

    @Override
    public void draw() {
        if (drawExplosion()) return;

        GameLib.setColor(COLOR);
        GameLib.drawCircle(x, y, SIZE / 2);
        GameLib.setColor(Color.WHITE);
        GameLib.drawCircle(x, y, SIZE / 4);
    }
}
