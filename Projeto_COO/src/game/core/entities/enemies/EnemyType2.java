package game.core.entities.enemies;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import game.core.entities.OwnerType;
import game.core.entities.Projectile;
import lib.GameLib;

// special enemy for fun
// dives straight down until it crosses a height threshold, then banks into a
// sideways curve toward whichever edge of the screen it started closer to. The instant it
// finishes banking (now flying level, sideways) it fires a 3-way fan of shots downward

public class EnemyType2 extends Enemy {
    // health designedly to be 1, because basic enemys dies with a single hit
    private static final int MAX_HEALTH = 1;
    private static final double SIZE = 16;

    private static final double DIVE_SPEED = 90;
    private static final double BANK_SPEED = 90;
    private static final double BANK_TRIGGER_HEIGHT_RATIO = 0.30;
    private static final double BANK_DURATION_MS = 500;

    private static final double PROJECTILE_SPEED = 260;
    private static final double FAN_SPREAD_RADIANS = Math.PI / 8;
    private static final int SHOOT_DAMAGE = 1;
    private static final int CONTACT_DAMAGE = 1;
    private static final double SHOOT_INTERVAL_MS = 0;

    private enum FlightPhase { DIVING, BANKING, CRUISING }

    private FlightPhase flightPhase = FlightPhase.DIVING;
    private double bankElapsedMs = 0;
    private int bankDirection = 1;
    private boolean fanFired = false;

    public EnemyType2(double x, double y) {
        super(x, y, SIZE, SIZE, MAX_HEALTH, Color.PINK, SHOOT_INTERVAL_MS, CONTACT_DAMAGE, SHOOT_DAMAGE);
        setVelocity(0, DIVE_SPEED);
    }

    @Override
    public void update(long delta) {
        if (updateExploding(delta)) return;

        super.update(delta);

        switch (flightPhase) {
            case DIVING:
                if (y >= GameLib.HEIGHT * BANK_TRIGGER_HEIGHT_RATIO) {
                    bankDirection = x < GameLib.WIDTH / 2.0 ? -1 : 1;
                    flightPhase = FlightPhase.BANKING;
                    bankElapsedMs = 0;
                }
                break;
            case BANKING:
                bankElapsedMs += delta;
                double t = Math.min(1.0, bankElapsedMs / BANK_DURATION_MS);
                setVelocity(bankDirection * BANK_SPEED * t, DIVE_SPEED * (1 - t));
                if (t >= 1.0) {
                    flightPhase = FlightPhase.CRUISING;
                }
                break;
            case CRUISING:
                break;
        }
    }

    @Override
    public void draw() {
        if (drawExplosion()) return;

        GameLib.setColor(Color.PINK);
        GameLib.drawCircle(x, y, SIZE/2);
    }

    @Override
    public List<Projectile> tryShoot(long currentTime, double targetX, double targetY) {
        if (fanFired || flightPhase != FlightPhase.CRUISING) {
            return null;
        }
        fanFired = true;
        // best part
        double[] angles = { Math.PI/2 - FAN_SPREAD_RADIANS, Math.PI/2, Math.PI/2 + FAN_SPREAD_RADIANS };
        List<Projectile> shots = new ArrayList<>(angles.length);

        for (double angle : angles) {
            double vx = Math.cos(angle) * PROJECTILE_SPEED;
            double vy = Math.sin(angle) * PROJECTILE_SPEED;
            shots.add(makeDefaultProjectile(x, y + SIZE / 2, vx, vy, OwnerType.ENEMY, Color.PINK));
        }

        return shots;
    }
}
