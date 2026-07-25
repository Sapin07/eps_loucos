package game.core.entities;

import java.awt.Color;

import lib.GameLib;

/**
 * Ao ser coletado, faz o jogador atirar 3 projéteis em leque a cada tiro,
 * por um período limitado (ver Player.activateTripleShot).
 */
public class TripleShotPowerUp extends PowerUp {
    private static final double SIZE = 18;
    private static final double FALL_SPEED = 80;
    private static final long TRIPLE_SHOT_DURATION_MS = 8000;

    public TripleShotPowerUp(double x, double y) {
        super(x, y, SIZE, SIZE, FALL_SPEED);
    }

    @Override
    protected void applyEffect(Player player) {
        player.activateTripleShot(TRIPLE_SHOT_DURATION_MS);
    }

    @Override
    public void draw() {
        GameLib.setColor(Color.YELLOW);
        GameLib.drawDiamond(x, y, SIZE / 2);
    }
}
