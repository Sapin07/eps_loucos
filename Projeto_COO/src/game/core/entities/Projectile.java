package game.core.entities;

import java.awt.Color;
import lib.GameLib;

public class Projectile extends Entity{
    private static final double DEFAULT_WIDTH = 4;
    private static final double DEFAULT_HEIGHT = 10;

    private final OwnerType owner;
    private final int damage;
    private final Color color;

    public Projectile(double x, double y, double vx, double vy, OwnerType owner, int damage, Color color){
        this(x, y, vx, vy, owner, damage, color, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public Projectile(double x, double y, double vx, double vy, OwnerType owner, int damage, Color color, double width, double height){
        super(x, y, width, height);
        this.owner = owner;
        this.damage = damage;
        this.color = color;
        setVelocity(vx, vy);
    }

    public OwnerType getOwner(){
        return owner;
    }

    public int getDamage(){
        return damage;
    }

    @Override
    public void draw(){
        GameLib.setColor(color);
        GameLib.drawLine(x, y - height / 2 , x, y + height / 2);
    }
}
