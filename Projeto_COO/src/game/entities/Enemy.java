package game.entities;
import java.awt.Color;

public abstract class Enemy extends Ship {
    private final double SHOOT_INTERVAL_MS;
    private long timeSinceLastShot = 0;

    protected Enemy(double x, double y, double width, double height, int maxHealth, Color color, double shootIntervalMS) {
        super(x, y, width, height, maxHealth, color);
        this.SHOOT_INTERVAL_MS = shootIntervalMS;
    }

    @Override
    public void update(long delta) {
        super.update(delta);
        timeSinceLastShot += delta;
    }

    protected final boolean readyToShoot() {
        if (timeSinceLastShot < SHOOT_INTERVAL_MS) {
            return false;
        }
        timeSinceLastShot = 0;
        return true;
    }

    // Each enemy decides how to shoot
    public abstract Projectile tryShoot(long currentTime, double targetX, double targetY);
}
