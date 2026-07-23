package game.entities;

import game.core.collision.Collidable;

public abstract class Entity extends GameObject implements Collidable {
    
    protected double vx;
    protected double vy;
    protected double width;
    protected double height;

    protected Entity(double x, double y, double width, double height){
        super(x, y);
        this.width = width;
        this.height = height;
    }

    public void setVelocity(double vx, double vy){
        this.vx = vx;
        this.vy = vy;
    }

    protected void move(long delta){
        double seconds = delta / 1000.0;
        x += vx * seconds;
        y += vy * seconds;
    }

    @Override
    public double getWidth(){
        return width;
    }

    @Override
    public double getHeight(){
        return height;
    }

    //default behavior
    @Override
    public void update(long delta){
        move(delta);

        if (isOutOfBounds()){
            deadactivate();
        }
    }

}
