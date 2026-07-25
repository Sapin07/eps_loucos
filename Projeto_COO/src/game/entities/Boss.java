package game.entities;

import java.awt.Color;

// Boss is just a special type of enemy that spawns at (x, y), moves toward
// (arenaX, arenaY) to enter the arena, and once there, fights using its
// own movement pattern and attack pattern until it dies (never leaves the screen).

public abstract class Boss extends Enemy {

    private boolean entering = true;
    private final double arenaX;
    private final double arenaY;

    private Player target;

    protected Boss(double x, double y, double width, double height, int maxHealth, Color color, double shootIntervalMs, double arenaX, double arenaY, double entrySpeed, int contactDamage) {
        super(x, y, width, height, maxHealth, color, shootIntervalMs, contactDamage);
        this.arenaX = arenaX;
        this.arenaY = arenaY;

        double dx = arenaX - x;
        double dy = arenaY - y;

        double distance = Math.sqrt(dx*dx + dy*dy);
        if (distance > 0) {
            setVelocity((dx/distance) * entrySpeed, (dy/distance) * entrySpeed);
        } else {
            setVelocity(0, 0);
            entering = false;
        }
    }

    public void setTarget(Player target) {
        this.target = target;
    }

    protected double getArenaX() {
        return arenaX;
    }

    protected double getArenaY() {
        return arenaY;
    }

    protected double getTargetX() {
        return target != null ? target.getX() : arenaX;
    }

    /** Y do alvo, ou o centro da arena se nenhum alvo foi definido ainda. */
    protected double getTargetY() {
        return target != null ? target.getY() : arenaY;
    }

    @Override
    public void update(long delta) {
        if (entering) {
            updateEntrance(delta);
        } else {
            super.update(delta);
            updateArenaMovement(delta);
        }
    }

    private void updateEntrance(long delta) {
        move(delta);

        boolean reachedX = Math.abs(x - arenaX) < 2;
        boolean reachedY = Math.abs(y - arenaY) < 2;

        if (reachedX && reachedY) {
            x = arenaX;
            y = arenaY;
            setVelocity(0, 0);
            entering = false;
        }
    }

    @Override
    protected boolean readyToShoot() {
        if (entering) {
            return false;
        }
        return super.readyToShoot();
    }

    // Each boss is responsible for defining its own movement pattern within the screen.
    protected abstract void updateArenaMovement(long delta);

    @Override
    protected boolean isOutOfBounds() {
        // A boss can't go out o f the bounds ;D
        return false;
    }

    @Override
    public boolean isDestroyedOnContact() {
        return false;
    }
}
