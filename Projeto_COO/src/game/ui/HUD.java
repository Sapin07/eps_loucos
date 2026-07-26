package game.ui;

import java.awt.Color;

import game.core.entities.Player;
import game.core.entities.enemies.Enemy;
import lib.GameLib;


// decides when and how draw the HealthBarr
public class HUD {
    private static final double BOTTOM_MARGIN = 20;
    private static final double PLAYER_ROW_GAP_ABOVE_BOSS_BAR = 34;

    private final HealthBar bossHealthBar;
    private final HealthSlots playerHealthSlots;

    public HUD(int playerMaxHealth) {
        double barWidth = GameLib.WIDTH * 0.6;
        double barHeight = 14;
        double barX = GameLib.WIDTH / 2.0;
        double barY = GameLib.HEIGHT - BOTTOM_MARGIN;
        this.bossHealthBar = new HealthBar(barX, barY, barWidth, barHeight, Color.RED, Color.DARK_GRAY, Color.WHITE);

        double playerRowBaseY = barY - PLAYER_ROW_GAP_ABOVE_BOSS_BAR;
        this.playerHealthSlots = new HealthSlots(playerMaxHealth, playerRowBaseY);
    }

    public void draw(Player player, Enemy boss) {
        playerHealthSlots.draw(player.getHealth());

        boolean bossVisible = boss != null && boss.isActive();

        if (bossVisible) {
            bossHealthBar.draw(boss.getHealth(), boss.getMaxHealth());
        }
    }
}
