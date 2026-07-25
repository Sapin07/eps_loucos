package game.entities.enemies;

import java.awt.Color;

import game.entities.Enemy;
import game.entities.OwnerType;
import game.entities.Projectile;
import lib.GameLib;

// Red Circle (Pink shoot) that shoots in the player's direction until dies or get out of the Screen, what comes first

// It would be interesting to make him move to the right.
// When he's about to leave the screen, he could turn around and start moving to the left.
// But at first he will only do the same move as his brother.

// But on second thought, every enemy eventually leaves the screen anyway (if I'm correct).
// So we can just make it move in a crazy way if we can't do this.

public class EnemyType2 extends Enemy {
    // health designedly to be 1, because basic enemys dies with a single hit
    private static final int MAX_HEALTH = 1;
    private static final double SIZE = 16;

    private static final double FALL_SPEED = 75;
    private static final double PROJECTILE_SPEED = 800;
    private static final double SHOOT_INTERVAL_MS = 600;

    private static final int SHOOT_DAMAGE = 1;

    public EnemyType2(double x, double y) {
        super(x, y, SIZE, SIZE, MAX_HEALTH, Color.RED, SHOOT_INTERVAL_MS);
        setVelocity(0, FALL_SPEED);
    }

    @Override
    public void draw() {
        GameLib.setColor(Color.RED);
        GameLib.drawCircle(x, y, SIZE/2);
    }

    @Override
    public Projectile tryShoot(long currentTime, double targetX, double targetY) {
        if (!readyToShoot()) {
            return null;
        }

        double dx = targetX - x;
        double dy = targetY - y;
        double distance = Math.sqrt(dx*dx + dy*dy); // esse sim é um pitágoras de respeito.

        double vx = (dx/distance) * PROJECTILE_SPEED;
        double vy = (dy/distance) * PROJECTILE_SPEED;

        resetShotTimer();
        return new Projectile(x, y + SIZE / 2, vx, vy, OwnerType.ENEMY, SHOOT_DAMAGE, Color.PINK);
    }
}

