package game.ui;

import java.awt.Color;
import game.core.Drawable;
import game.core.Updatable;

public class StarField implements Drawable, Updatable {

    private static final int FAR_STAR_COUNT = 50;
    private static final double FAR_SPEED = 0.045;
    private static final double FAR_SIZE = 2;
    private static final Color FAR_COLOR = Color.DARK_GRAY;

    private static final int NEAR_STAR_COUNT = 20;
    private static final double NEAR_SPEED = 0.070;
    private static final double NEAR_SIZE = 3;
    private static final Color NEAR_COLOR = Color.GRAY;

    private final StarLayer farLayer;
    private final StarLayer nearLayer;

    public StarField() {
        this.farLayer = new StarLayer(FAR_STAR_COUNT, FAR_SPEED, FAR_SIZE, FAR_COLOR);
        this.nearLayer = new StarLayer(NEAR_STAR_COUNT, NEAR_SPEED, NEAR_SIZE, NEAR_COLOR);
    }

    @Override
    public void update(long delta) {
        farLayer.update(delta);
        nearLayer.update(delta);
    }

    @Override
    public void draw() {
        farLayer.draw();
        nearLayer.draw();
    }
}
