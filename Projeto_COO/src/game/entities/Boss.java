package game.entities;
import java.awt.Color;

public abstract class Boss extends Enemy {
    protected boolean hasEnteredArena = false;

    protected Boss(double x, double y, double width, double height, int maxHealth, Color color) {
        super(x, y, width, height, maxHealth, color);
    }

    @Override
    public void update(long delta) {
        super.update(delta);
    }

    @Override
    protected boolean isOutOfBounds() {
        return false;
    }
}
