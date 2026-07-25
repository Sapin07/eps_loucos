package game.entities;

import java.awt.Color;
import lib.GameLib;

// Green Circle that spaws (x,y) shoots up-down (and moves the same direction)
// until dies or leaves the screen, what comes first

public class EnemyType1 extends Enemy {
    // health designedly to be 1, because basic enemys dies with a single hit
    private static final int MAX_HEALTH = 1;
    private static final double SIZE = 20;

    private static final double FALL_SPEED = 100;
    private static final double PROJECTILE_SPEED = 1000;
    private static final double SHOOT_INTERVAL_MS = 500;

    private static final int SHOOT_DAMAGE = 1;

    public EnemyType1(double x, double y) {
        super(x, y, SIZE, SIZE, MAX_HEALTH, Color.GREEN, SHOOT_INTERVAL_MS);
        setVelocity(0, FALL_SPEED);
    }

    @Override
    public void draw() {
        GameLib.setColor(Color.GREEN);
        GameLib.drawCircle(x, y, SIZE/2);
    }

    @Override
    public Projectile tryShoot(long currentTime, double targetX, double targetY) {
        if (!readyToShoot()) {
            return null;
        }

        return new Projectile(x, y + SIZE / 2, 0, PROJECTILE_SPEED, OwnerType.ENEMY, SHOOT_DAMAGE, Color.RED);
    }
}
