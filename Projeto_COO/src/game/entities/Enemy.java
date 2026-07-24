package game.entities;
import java.awt.Color;

public abstract class Enemy extends Ship {
    protected Enemy(double x, double y, double width, double height, int maxHealth, Color color) {
        super(x, y, width, height, maxHealth, color);
    }

    // Each enemy decides how to shoot
    public abstract Projectile shoot(long currentTime, double targetX, double targetY);
}
