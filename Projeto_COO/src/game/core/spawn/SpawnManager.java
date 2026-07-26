package game.core.spawn;

import game.config.spawn.EntitySpec;
import game.core.entities.enemies.Boss;
import game.core.entities.enemies.BossType1;
import game.core.entities.enemies.BossType2;
import game.core.entities.enemies.Enemy;
import game.core.entities.enemies.EnemyType1;
import game.core.entities.enemies.EnemyType2;
import game.core.entities.powerUps.PowerUp;
import game.core.entities.powerUps.ShieldPowerUp;
import game.core.entities.powerUps.TripleShotPowerUp;

// Translates the declarative EntitySpec read from a phase file into concrete, ready-to-play entities.
public class SpawnManager {

    public Enemy createEnemy(EntitySpec spec) {
        switch (spec.getType()) {
            case 1:
                return new EnemyType1(spec.getX(), spec.getY());
            case 2:
                return new EnemyType2(spec.getX(), spec.getY());
            default:
                throw new IllegalArgumentException("Tipo de inimigo desconhecido: " + spec.getType());
        }
    }

    public Boss createBoss(EntitySpec spec) {
        switch (spec.getType()) {
            case 1:
                return new BossType1(spec.getX(), spec.getY(), spec.getLifePoints());
            case 2:
                return new BossType2(spec.getX(), spec.getY(), spec.getLifePoints());
            default:
                throw new IllegalArgumentException("Tipo de chefe desconhecido: " + spec.getType());
        }
    }

    public PowerUp createPowerUp(EntitySpec spec) {
        switch (spec.getType()) {
            case 1:
                return new ShieldPowerUp(spec.getX(), spec.getY());
            case 2:
                return new TripleShotPowerUp(spec.getX(), spec.getY());
            default:
                throw new IllegalArgumentException("Tipo de power-up desconhecido: " + spec.getType());
        }
    }
}
