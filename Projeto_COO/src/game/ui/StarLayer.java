package game.ui;

import java.awt.Color;
import java.util.Random;
import game.core.Drawable;
import game.core.Updatable;
import lib.GameLib;

public class StarLayer implements Drawable, Updatable {

    private final double[] starX;
    private final double[] starY;
    private final double SPEED;
    private final double SIZE;
    private final Color color;

    private double offset = 0;

    public StarLayer(int starCount, double speed, double size, Color color) {
        this.starX = new double[starCount];
        this.starY = new double[starCount];
        this.SPEED = speed;
        this.SIZE = size;
        this.color = color;

        Random random = new Random();
        for (int i = 0; i < starCount; i++) {
            starX[i] = random.nextDouble() * GameLib.WIDTH;
            starY[i] = random.nextDouble() * GameLib.HEIGHT;
        }
    }

    @Override
    public void update(long delta) {
        offset += SPEED * delta;
    }

    @Override
    public void draw() {
        GameLib.setColor(color);
        for (int i = 0; i < starX.length; i++) {
            double y = (starY[i] + offset) % GameLib.HEIGHT;
            GameLib.fillRect(starX[i], y, SIZE, SIZE);
        }
    }
}
