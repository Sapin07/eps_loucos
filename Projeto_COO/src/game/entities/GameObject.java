package game.entities;

import game.core.Drawable;
import game.core.Updatable;
import lib.GameLib;

public abstract class GameObject implements Drawable, Updatable {
    // Margin to consider a object out of screen, avoid the object to pop off when it still visible 
    private static final double OUT_OF_BOUNDS_MARGIN = 40; // need to test value

    protected double x;
    protected double y;
    protected boolean active;

    protected GameObject(double x, double y){
        this.x = x;
        this.y = y;
        this.active = true;
    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }

    public boolean isActive(){
        return active;
    }

    public void deadactivate(){
        this.active = false;
    }
    protected boolean isOutOfBounds(){
        // the idea is some entities like power ups, projectiles and/or normal enemies can get out of the screen
        // when this happen, we need to handle with (we cannot spawn 2000 entities to be out of screen, right?)
        return x < - OUT_OF_BOUNDS_MARGIN 
            || x > GameLib.WIDTH + OUT_OF_BOUNDS_MARGIN
            || y < - OUT_OF_BOUNDS_MARGIN
            || y > GameLib.HEIGHT + OUT_OF_BOUNDS_MARGIN;
    }

}
