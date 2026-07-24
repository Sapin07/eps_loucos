package game.entities;

import java.awt.Color;
import lib.GameLib;

// Green Circle that shoots up-down until dies or , what comes first

public class EnemyType1 extends Enemy {
    // health designedly to be 1, because basic enemys dies with a single hit
    private static final int MAX_HEALTH = 1;
    private static final double SIZE = 20;

    private static final double FALL_SPEED = 100;
    private static final double PROJECTILE_SPEED = 1000;
    private static final double SHOOT_INTERVAL_MS = 500;
    private static final int SHOOT_DAMAGE = 1;

    private long timeSinceLastShot = 0;

    public EnemyType1(double x, double y) {
        super(x, y, SIZE, SIZE, MAX_HEALTH, Color.GREEN);
        setVelocity(0, FALL_SPEED);
    }

    @Override
    public void update(long delta) {
        super.update(delta);
        timeSinceLastShot += delta;
    }

    @Override
    public void draw() {
        GameLib.setColor(Color.GREEN);
        GameLib.drawCircle(x, y, SIZE/2);
    }

    @Override
    public Projectile shoot(long currentTime, double targetX, double targetY) {
        if (timeSinceLastShot < SHOOT_INTERVAL_MS) {
            return null;
        }

        timeSinceLastShot = 0;
        return new Projectile(x, y + SIZE / 2, 0, PROJECTILE_SPEED, OwnerType.ENEMY, SHOOT_DAMAGE, Color.RED);
    }
}
