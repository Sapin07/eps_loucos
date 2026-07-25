package game.core.entities;

public abstract class PowerUp extends Entity {
    protected PowerUp(double x, double y, double width, double height, double fallSpeed) {
        super(x, y, width, height);
        setVelocity(0, fallSpeed);
    }

    public final void collectBy(Player player) {
        if (!isActive()) {
            return;
        }

        applyEffect(player);
        deadactivate();
    }
    
    protected abstract void applyEffect(Player player);
}
