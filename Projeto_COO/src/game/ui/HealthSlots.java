package game.ui;

import java.awt.Color;
import lib.GameLib;

public class HealthSlots {
    private static final double SLOT_SIZE = 10;
    private static final double SLOT_SPACING = 3;
    private static final double MARGIN_X = 8;

    private static final Color FILLED_COLOR = Color.GREEN;
    private static final Color EMPTY_COLOR = Color.DARK_GRAY;
    private static final Color BORDER_COLOR = Color.WHITE;

    private final int maxHealth;
    private final int slotsPerRow;
    private final double baseCenterY;

    public HealthSlots(int maxHealth, double baseCenterY) {
        this.maxHealth = Math.max(0, maxHealth);
        this.baseCenterY = baseCenterY;

        double step = SLOT_SIZE + SLOT_SPACING;
        int perRow = (int) ((GameLib.WIDTH - 2 * MARGIN_X + SLOT_SPACING) / step);
        this.slotsPerRow = Math.max(1, perRow);
    }

    public void draw(int currentHealth) {
        for (int i = 0; i < maxHealth; i++) {
            int row = i / slotsPerRow;
            int col = i % slotsPerRow;

            double cx = MARGIN_X + SLOT_SIZE / 2 + col * (SLOT_SIZE + SLOT_SPACING);
            double cy = baseCenterY - row * (SLOT_SIZE + SLOT_SPACING);

            GameLib.setColor(i < currentHealth ? FILLED_COLOR : EMPTY_COLOR);
            GameLib.fillRect(cx, cy, SLOT_SIZE, SLOT_SIZE);

            GameLib.setColor(BORDER_COLOR);
        }
    }
}
