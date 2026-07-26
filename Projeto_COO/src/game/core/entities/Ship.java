package game.core.entities;

import java.awt.Color;
import game.core.Damageable;
import lib.GameLib;

public abstract class Ship extends Entity implements Damageable {
    protected int health;
    protected final int maxHealth;
    protected Color color;

    private boolean exploding = false;
    private long currentExplosion;
    private final long explodingDuration = 1000;

    protected Ship(double x, double y, double width, double height, int maxHealth, Color color){
        super(x, y, width, height);
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.color = color;
    }

    @Override
    public void takeDamage(int damage){
        if (exploding) return;

        health -= damage;

        if (health <= 0){
            health = 0;
            exploding = true;
            currentExplosion = 0;
        }
    }

    protected final boolean updateExploding(long delta) {
        if (!exploding) return false;
        
        currentExplosion += delta;

        if (currentExplosion >= explodingDuration) deadactivate();

        return true;
    }

    protected final boolean drawExplosion() {
        if (!exploding) {
            return false;
        }
        double percentOfCurrentExplo = Math.min(1.0, (double) currentExplosion / explodingDuration);
        GameLib.drawExplosion(x, y, percentOfCurrentExplo);
        return true;
    }

    @Override
    public boolean isAlive(){
        return health > 0;
    }

    public boolean isExploding() {
        return exploding;
    }
 
    public int getHealth(){
        return health;
    }

    public int getMaxHealth(){
        return maxHealth;
    }
}
