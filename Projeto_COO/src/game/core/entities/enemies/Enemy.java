package game.core.entities.enemies;

import java.awt.Color;
import java.util.List;

import game.core.entities.OwnerType;
import game.core.entities.Projectile;
import game.core.entities.Ship;

public abstract class Enemy extends Ship {
    private final double shootIntervalMs;
    private long timeSinceLastShot = 0;
    private final int contactDamage;
    private final int shotDamage;

    protected Enemy(double x, double y, double width, double height, int maxHealth, Color color,
        double shootIntervalMs, int contactDamage, int shotDamage) {
        super(x, y, width, height, maxHealth, color);
        this.shootIntervalMs = shootIntervalMs;
        this.contactDamage = contactDamage;
        this.shotDamage = shotDamage;
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
        // this is for basic projectile 
        timeSinceLastShot = 0;
        return new Projectile(px, py, pvx, pvy, owner, shotDamage, color);
    }

    protected Projectile makeProjectile(double px, double py, double pvx, double pvy, OwnerType owner, Color color, double width, double height) {
        // this for specials
        timeSinceLastShot = 0;
        return new Projectile(px, py, pvx, pvy, owner, shotDamage, color, width, height);
    }

    public int getContactDamage() {
        return contactDamage;
    }

    public boolean diesOnPlayerContact() {
        return true;
    }

    // Enemies must implement their shooting behaviour a burst attack (fan/triple shot) returns
    // more than one projectile in the same call, a single shot returns a one-element list, and
    // "not ready to fire yet" returns null.
    public abstract List<Projectile> tryShoot(long currentTime, double targetX, double targetY);
}
