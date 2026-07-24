package game.entities;

import java.awt.Color;
import lib.GameLib;

// BossType2 is a boss that chases the player's x position within its arena,
// staying at a fixed y, and fires aimed shots toward the player at a faster
// interval than BossType1.
public class BossType2 extends Boss {
    private static final double SIZE = 54;
    private static final Color COLOR = Color.ORANGE;
    private static final double SHOOT_INTERVAL_MS = 500;
    private static final int SHOOT_DAMAGE = 1;
    private static final double PROJECTILE_SPEED = 500;
    private static final double ENTRY_SPEED = 180;

    private static final double CHASE_SPEED = 140;

    public BossType2(double arenaX, double arenaY, int maxHealth) {
        super(arenaX, -SIZE, SIZE, SIZE, maxHealth, COLOR, SHOOT_INTERVAL_MS, arenaX, arenaY, ENTRY_SPEED);
    }

    @Override
    protected void updateArenaMovement(long delta) {
        double seconds = delta / 1000.0;
        double dx = getTargetX() - x;
        double maxStep = CHASE_SPEED * seconds;

        if (Math.abs(dx) <= maxStep) {
            x = getTargetX();
        } else {
            x += Math.signum(dx) * maxStep;
        }

        y = getArenaY();
    }

    @Override
    public Projectile tryShoot(long currentTime, double targetX, double targetY) {
        if (!readyToShoot()) {
            return null;
        }

        double dx = getTargetX() - x;
        double dy = getTargetY() - (y + height / 2);
        double distance = Math.sqrt(dx*dx + dy*dy);
        if (distance == 0) {
            distance = 1;
        }

        double vx = (dx / distance) * PROJECTILE_SPEED;
        double vy = (dy / distance) * PROJECTILE_SPEED;

        return new Projectile(x, y + height / 2, vx, vy, OwnerType.ENEMY, SHOOT_DAMAGE, Color.ORANGE);
    }

    @Override
    public void draw() {
        GameLib.setColor(COLOR);
        GameLib.drawDiamond(x, y, SIZE / 2);
        GameLib.setColor(Color.RED);
        GameLib.drawDiamond(x, y, SIZE / 4);
    }
}