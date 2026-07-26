package game.core.entities.enemies;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import game.core.entities.OwnerType;
import game.core.entities.Projectile;
import lib.GameLib;

public class BossType2 extends Boss {
    // the bosses was too boring so I decide to not sleep and do some cool stuff kkkkkkkkkkk
    // this is getting wild
    private static final double SIZE = 70;
    private static final Color COLOR = Color.ORANGE;

    private static final double DIVE_AMPLITUDE = 120;
    private static final double DIVE_PERIOD_MS = 3000;

    private static final double PATROL_HALF_RANGE = 200;
    private static final double DRIFT_SPEED = 60;

    private static final double PROJECTILE_SPEED = 320;
    private static final double FAN_SPREAD_RADIANS = Math.PI / 8;
    private static final int SHOOT_DAMAGE = 2;
    private static final int CONTACT_DAMAGE = 1;
    private static final double SHOOT_INTERVAL_MS = 0;

    private static final double ENTRY_SPEED = 180;

    private double cycleTimeMs = 0;
    private double previousCyclePhase = 0;
    private int driftDirection = 1;
    private boolean pendingFanShot = false;

    public BossType2(double arenaX, double arenaY, int maxHealth) {
        super(arenaX, -SIZE, SIZE, SIZE, maxHealth, COLOR, SHOOT_INTERVAL_MS, CONTACT_DAMAGE, SHOOT_DAMAGE, arenaX, arenaY, ENTRY_SPEED);
    }

    @Override
    protected void updateArenaMovement(long delta) {
        double seconds = delta / 1000.0;

        cycleTimeMs += delta;
        double cyclePhase = (cycleTimeMs % DIVE_PERIOD_MS) / DIVE_PERIOD_MS;
        y = getArenaY() + Math.sin(cyclePhase * 2 * Math.PI) * DIVE_AMPLITUDE;

        x += driftDirection * DRIFT_SPEED * seconds;
        double left = getArenaX() - PATROL_HALF_RANGE;
        double right = getArenaX() + PATROL_HALF_RANGE;
        if (x <= left) {
            x = left;
            driftDirection = 1;
        } else if (x >= right) {
            x = right;
            driftDirection = -1;
        }

        // detect the crossing rather than a fixed window so it fires exactly once per cycle
        // regardless of frame rate.
        if (previousCyclePhase < 0.25 && cyclePhase >= 0.25) {
            pendingFanShot = true;
        }
        previousCyclePhase = cyclePhase;
    }

    @Override
    public List<Projectile> tryShoot(long currentTime, double targetX, double targetY) {
        if (!pendingFanShot) {
            return null;
        }
        pendingFanShot = false;

        double[] angles = { Math.PI/2 - FAN_SPREAD_RADIANS, Math.PI/2, Math.PI/2 + FAN_SPREAD_RADIANS };
        List<Projectile> shots = new ArrayList<>(angles.length);

        for (double angle : angles) {
            double vx = Math.cos(angle) * PROJECTILE_SPEED;
            double vy = Math.sin(angle) * PROJECTILE_SPEED;
            shots.add(makeDefaultProjectile(x, y + height / 2, vx, vy, OwnerType.ENEMY, COLOR));
        }

        return shots;
    }

    @Override
    public void draw() {
        if (drawExplosion()) return;

        GameLib.setColor(COLOR);
       
        GameLib.drawDiamond(x, y, SIZE / 2);
        GameLib.setColor(Color.RED);
        GameLib.drawDiamond(x, y, SIZE / 4);
    }
}
