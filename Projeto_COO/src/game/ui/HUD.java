package game.ui;

import java.awt.Color;
import game.core.entities.enemies.Enemy;
import lib.GameLib;


// decides when and how draw the HealthBarr
public class HUD {

    private final HealthBar bossHealthBar;

    public HUD() {
        double barWidth = GameLib.WIDTH * 0.6;
        double barHeight = 14;
        double barX = GameLib.WIDTH / 2.0;
        double barY = 24;
        this.bossHealthBar = new HealthBar(barX, barY, barWidth, barHeight, Color.RED, Color.DARK_GRAY, Color.WHITE);
    }

    public void draw(Enemy boss) {
        if (boss != null && boss.isActive()) {
            bossHealthBar.draw(boss.getHealth(), boss.getMaxHealth());
        }
    }
}
