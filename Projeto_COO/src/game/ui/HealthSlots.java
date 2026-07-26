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
        int totalRows = (int) Math.ceil((double) maxHealth / slotsPerRow);

        for (int row = 0; row < totalRows; row++) {
            int slotsInRow = Math.min(slotsPerRow, maxHealth - row * slotsPerRow);
            double rowWidth = slotsInRow * SLOT_SIZE + (slotsInRow - 1) * SLOT_SPACING;
            double startX = GameLib.WIDTH / 2.0 - rowWidth / 2.0 + SLOT_SIZE / 2.0;

            for (int col = 0; col < slotsInRow; col++) {
                int i = row * slotsPerRow + col;
                double cx = startX + col * (SLOT_SIZE + SLOT_SPACING);
                double cy = baseCenterY - row * (SLOT_SIZE + SLOT_SPACING);

                GameLib.setColor(i < currentHealth ? FILLED_COLOR : EMPTY_COLOR);
                GameLib.fillRect(cx, cy, SLOT_SIZE, SLOT_SIZE);

                GameLib.setColor(BORDER_COLOR);
                double left = cx - SLOT_SIZE / 2;
                double right = cx + SLOT_SIZE / 2;
                double top = cy - SLOT_SIZE / 2;
                double bottom = cy + SLOT_SIZE / 2;
                GameLib.drawLine(left, top, right, top);
                GameLib.drawLine(right, top, right, bottom);
                GameLib.drawLine(right, bottom, left, bottom);
                GameLib.drawLine(left, bottom, left, top);
            }
        }
    }
}
