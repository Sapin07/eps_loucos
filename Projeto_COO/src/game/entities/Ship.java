package game.entities;

import java.awt.Color;
import game.core.Damageable;

public abstract class Ship extends Entity implements Damageable {
    protected int health;
    protected final int maxHealth;
    protected Color color;

    protected Ship(double x, double y, double width, double height, int maxHealth, Color color){
        super(x, y, width, height);
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.color = color;
    }

    @Override
    public void takeDamage(int damage){
        health -= damage;

        if (health <= 0){
            health = 0;
            deadactivate();
        }
    }

    @Override
    public boolean isAlive(){
        return health > 0;
    }

    public int getHealth(){
        return health;
    }

    public int getMaxHealth(){
        return maxHealth;
    }
}
