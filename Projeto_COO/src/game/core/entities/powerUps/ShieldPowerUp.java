package game.core.entities.powerUps;

import java.awt.Color;

import game.core.entities.Player;
import lib.GameLib;

/**
 * Ao ser coletado, concede ao jogador um escudo temporário que o torna
 * invulnerável a dano por um período limitado (ver Player.activateShield).
 */
public class ShieldPowerUp extends PowerUp {
    private static final double SIZE = 18;
    private static final double FALL_SPEED = 80;
    private static final long SHIELD_DURATION_MS = 5000;

    public ShieldPowerUp(double x, double y) {
        super(x, y, SIZE, SIZE, FALL_SPEED);
    }

    @Override
    protected void applyEffect(Player player) {
        player.activateShield(SHIELD_DURATION_MS);
    }

    @Override
    public void draw() {
        GameLib.setColor(Color.CYAN);
        GameLib.drawDiamond(x, y, SIZE / 2);
    }
}
