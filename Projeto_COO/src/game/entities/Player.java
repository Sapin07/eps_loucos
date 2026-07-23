package game.entities;

import java.awt.Color;

import lib.GameLib;

public class Player extends Ship {
    // todo:
    // maybe on try shoot just add on entitys list
    //
    // to adjust, maybe to read from env file
    private static final double SIZE = 10;
    private static final double SPEED = 220;
    private static final long SHOT_INTERVAL_MS = 250;
    private static final int SHOT_DAMAGE = 1;
    private static final int SHOT_SPEED = -400;
    private static final double DIAGONAL_FACTOR = Math.sqrt(2) /2; //pythagoras wow

    private long timeSinceLastShot = SHOT_INTERVAL_MS;

    public Player(double x, double y, int maxHealth) {
        super(x, y, SIZE * 2, SIZE * 2, maxHealth, Color.WHITE);
    }

    @Override
    public void update(long delta) {
        readInput(delta);
        clampToScreen();
        timeSinceLastShot += delta;
    }

    private void readInput(long delta){
        double seconds = delta / 1000.0;
        double dx = 0, dy = 0;

        if(GameLib.iskeyPressed(GameLib.KEY_UP)) dy -= 1;
        if (GameLib.iskeyPressed(GameLib.KEY_DOWN))  dy += 1;
        if (GameLib.iskeyPressed(GameLib.KEY_LEFT))  dx -= 1;
        if (GameLib.iskeyPressed(GameLib.KEY_RIGHT)) dx += 1;
 
        if (dx != 0 && dy != 0) {
            dx *= DIAGONAL_FACTOR;
            dy *= DIAGONAL_FACTOR;
        }
 
        x += dx * SPEED * seconds;
        y += dy * SPEED * seconds;
    }

    private void clampToScreen(){
        double halfSize = width / 2;
        x = Math.max(halfSize, Math.min(GameLib.WIDTH - halfSize, x));
        y = Math.max(halfSize, Math.min(GameLib.HEIGHT - halfSize, y));
    }

    public Projectile tryShoot(){
        if (!GameLib.iskeyPressed(GameLib.KEY_CONTROL) || timeSinceLastShot < SHOT_INTERVAL_MS) {
            return null;
        }

        timeSinceLastShot = 0;
        return new Projectile(x, y - height / 2, 0, SHOT_SPEED, null, SHOT_DAMAGE, Color.CYAN);
    }

    @Override 
    public void draw(){
        GameLib.setColor(color);
        GameLib.drawPlayer(x, y, SIZE);
    }

    @Override
    protected boolean isOutOfBounds(){
        return false;
    }
}
