package game.entities;

import java.awt.Color;
import lib.GameLib;

public class Projectile extends Entity{
    private static final double WIDTH = 4;
    private static final double HEIGHT = 10;

    private final OwnerType owner;
    private final int damage;
    private final Color color;

    public Projectile(double x, double y, double vx, double vy, OwnerType owner, int damage, Color color){
        super(x, y, WIDTH, HEIGHT);
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
