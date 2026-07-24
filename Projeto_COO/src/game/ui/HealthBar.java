package game.ui;

import java.awt.Color;
import lib.GameLib;

// Render a generic health bar (player or boss) 
// given a current/max health pair. x and y represent the center of the bar.

public class HealthBar {

    private final double x;
    private final double y;
    private final double width;
    private final double height;

    private final Color foreground;
    private final Color background = Color.DARK_GRAY;
    private final Color border = Color.WHITE; 

    public HealthBar(double x, double y, double width, double height, Color foreground, Color background, Color border) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.foreground = foreground;
    }

    public void draw(int currentHealth, int maxHealth) {
        double ratio = proportionOf(currentHealth, maxHealth);

        GameLib.setColor(background);
        GameLib.fillRect(x, y, width, height);

        if (ratio > 0) {
            double filledWidth = width * ratio;
            double filledX = (x - width / 2) + filledWidth / 2;
            GameLib.setColor(foreground);
            GameLib.fillRect(filledX, y, filledWidth, height);
        }

        GameLib.setColor(border);
        drawBorder();
    }

    private double proportionOf(int currentHealth, int maxHealth) {
        if (maxHealth <= 0) {
            return 0;
        }
        double proportion = (double) currentHealth / maxHealth;
        return Math.max(0, Math.min(1, proportion));
    }

    private void drawBorder() {
        double left = x - width / 2;
        double right = x + width / 2;
        double top = y - height / 2;
        double bottom = y + height / 2;

        GameLib.drawLine(left, top, right, top);
        GameLib.drawLine(right, top, right, bottom);
        GameLib.drawLine(right, bottom, left, bottom);
        GameLib.drawLine(left, bottom, left, top);
    }
}
