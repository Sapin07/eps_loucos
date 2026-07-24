package game.entities;

import java.awt.Color;
import java.util.List;
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
    
    private static final double TRIPLE_SHOT_DISTX = 15;
    
    private long timeSinceLastShot = SHOT_INTERVAL_MS;

    private long shieldTimeRemaining = 0;
    private long tripleShotTimeRemaining = 0;

    public Player(double x, double y, int maxHealth) {
        super(x, y, SIZE * 2, SIZE * 2, maxHealth, Color.WHITE);
    }

    @Override
    public void update(long delta) {
        readInput(delta);
        clampToScreen();
        timeSinceLastShot += delta;
        shieldTimeRemaining = Math.max(0, shieldTimeRemaining - delta);
        tripleShotTimeRemaining = Math.max(0, tripleShotTimeRemaining - delta);
    }

    private void readInput(long delta){
        double seconds = delta / 1000.0;
        double dx = 0, dy = 0;

        if (GameLib.iskeyPressed(GameLib.KEY_UP)) dy -= 1;
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

    public List<Projectile> tryShoot(){
        if (!GameLib.iskeyPressed(GameLib.KEY_CONTROL) || timeSinceLastShot < SHOT_INTERVAL_MS) {
            return List.of();
        }

        timeSinceLastShot = 0;

        if (hasTripleShot()) {
            return List.of(
                new Projectile(x - TRIPLE_SHOT_DISTX, y - height / 2, 0, SHOT_SPEED, OwnerType.PLAYER, SHOT_DAMAGE, Color.CYAN),
                new Projectile(x, y - height / 2, 0, SHOT_SPEED, OwnerType.PLAYER, SHOT_DAMAGE, Color.CYAN),
                new Projectile(x + TRIPLE_SHOT_DISTX, y - height / 2, 0, SHOT_SPEED, OwnerType.PLAYER, SHOT_DAMAGE, Color.CYAN)
            );
        }

        return List.of(new Projectile(x, y - height / 2, 0, SHOT_SPEED, OwnerType.PLAYER, SHOT_DAMAGE, Color.CYAN));
    }

    public void activateShield(long durationMs) {
        shieldTimeRemaining = durationMs;
    }

    public boolean isShielded() {
        return shieldTimeRemaining > 0;
    }

    public void activateTripleShot(long durationMs) {
        tripleShotTimeRemaining = durationMs;
    }

    public boolean hasTripleShot() {
        return tripleShotTimeRemaining > 0;
    }

    @Override
    public void takeDamage(int damage) {
        if (isShielded()) {
            return;
        }
        super.takeDamage(damage);
    }

    @Override 
    public void draw(){
        GameLib.setColor(isShielded() ? Color.CYAN : color);
        GameLib.drawPlayer(x, y, SIZE);
    }

    @Override
    protected boolean isOutOfBounds(){
        return false;
    }
}
