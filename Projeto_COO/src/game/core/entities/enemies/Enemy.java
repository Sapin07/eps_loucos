package game.core.entities.enemies;

import java.awt.Color;

import game.core.entities.OwnerType;
import game.core.entities.Projectile;
import game.core.entities.Ship;

public abstract class Enemy extends Ship {
    private static final int DEFAULT_SHOT_DAMAGE = 1;
    private static final int DEFAULT_CONTACT_DAMAGE = 1;

    private final double shootIntervalMs;
    private long timeSinceLastShot = 0;
    private final int contactDamage;
    private final int shotDamage;

    protected Enemy(double x, double y, double width, double height, int maxHealth, Color color, double shootIntervalMs) {
        super(x, y, width, height, maxHealth, color);
        this.shootIntervalMs = shootIntervalMs;
        this.contactDamage = DEFAULT_CONTACT_DAMAGE;
        this.shotDamage = DEFAULT_SHOT_DAMAGE;
    }

    @Override
    public void update(long delta) {
        if (updateExploding(delta)) return;

        super.update(delta);
        timeSinceLastShot += delta;
    }

    protected boolean readyToShoot() {
        // decided to maintain it for the possible behavior component this can be useful
        if (isExploding()) return false;

        return timeSinceLastShot >= shootIntervalMs;
    }

    protected Projectile makeDefaultProjectile(double px, double py, double pvx, double pvy, OwnerType owner, Color color) {
        // reset shot timer when creating a projectile via this helper
        timeSinceLastShot = 0;
        return new Projectile(px, py, pvx, pvy, owner, shotDamage, color);
    }

    protected void resetShotTimer() {
        timeSinceLastShot = 0;
    }

    public int getContactDamage() {
        return contactDamage;
    }

    // Enemies must implement their shooting behaviour
    public abstract Projectile tryShoot(long currentTime, double targetX, double targetY);
}
