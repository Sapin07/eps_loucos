package game.entities;

import java.awt.Color;
import lib.GameLib;

// BossType1 is a boss that patrols side to side within its arena, bouncing
// between a fixed left/right range, and fires straight down at a fixed
// interval, ignoring the player's position entirely.
public class BossType1 extends Boss {

    private static final double SIZE = 60;
    private static final Color COLOR = Color.MAGENTA;

    private static final double SHOOT_INTERVAL_MS = 900;
    private static final int SHOOT_DAMAGE = 2;
    private static final double PROJECTILE_SPEED = 350;
    
    private static final double ENTRY_SPEED = 150;

    private static final double PATROL_HALF_RANGE = 150;
    private static final double PATROL_SPEED = 120;

    private int direction = 1;

    public BossType1(double arenaX, double arenaY, int maxHealth) {
        // he starts off the screen and goes down vertical(we can change that)
        super(arenaX, -SIZE, SIZE, SIZE, maxHealth, COLOR, SHOOT_INTERVAL_MS, arenaX, arenaY, ENTRY_SPEED);
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
    public Projectile tryShoot(long currentTime, double targetX, double targetY) {
        if (!readyToShoot()) {
            return null;
        }

        return new Projectile(x, y + height / 2, 0, PROJECTILE_SPEED, OwnerType.ENEMY, SHOOT_DAMAGE, Color.MAGENTA);
    }

    @Override
    public void draw() {
        GameLib.setColor(COLOR);
        GameLib.drawCircle(x, y, SIZE / 2);
        GameLib.setColor(Color.WHITE);
        GameLib.drawCircle(x, y, SIZE / 4);
    }
}