package game.entities;
import java.awt.Color;

import java.awt.Color;

import lib.GameLib;
 
public class Enemy extends Ship{
    private static final double SIZE = 14;
    private static final long SHOT_INTERVAL_MS = 1500;
    private static final int SHOT_DAMAGE = 1;
    private static final int CONTACT_DAMAGE = 1;

    private long timeSinceLastShot = 0;

    public Enemy(double x, double y, int health, double vy){
        super(x, y, SIZE*2, SIZE*2, health, Color.RED);
        setVelocity(0, vy);
    }

    @Override 
    public void update(long delta){
        super.update(delta);
        timeSinceLastShot += delta;
    }

    public Projectile tryShoot(){
        if (timeSinceLastShot < SHOT_INTERVAL_MS) {
            return null;
        }

        timeSinceLastShot = 0;
        return new Projectile(x, y, vx, vy, OwnerType.ENEMY, SHOT_DAMAGE, Color.ORANGE);
    }

    public int getContactDamage(){
        return CONTACT_DAMAGE;
    }

    @Override
    public void draw(){
        GameLib.setColor(color);
        GameLib.drawDiamond(x, y, SIZE);
    }
}
