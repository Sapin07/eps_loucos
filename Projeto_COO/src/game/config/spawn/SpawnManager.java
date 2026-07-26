package game.config.spawn;

import game.core.entities.enemies.Boss;
import game.core.entities.enemies.BossType1;
import game.core.entities.enemies.BossType2;
import game.core.entities.enemies.Enemy;
import game.core.entities.enemies.EnemyType1;
import game.core.entities.enemies.EnemyType2;
import game.core.entities.powerUps.PowerUp;
import game.core.entities.powerUps.ShieldPowerUp;
import game.core.entities.powerUps.TripleShotPowerUp;

// Translates the declarative spawn entries read from a phase file into concrete, ready-to-play entities.
public class SpawnManager {

    public Enemy createEnemy(EnemySpawnEntry spec) {
        switch (spec.getKind()) {
            case TYPE_1:
                return new EnemyType1(spec.getX(), spec.getY());
            case TYPE_2:
                return new EnemyType2(spec.getX(), spec.getY());
            default:
                throw new IllegalArgumentException("Tipo de inimigo desconhecido: " + spec.getKind());
        }
    }

    public Boss createBoss(BossSpawnEntry spec) {
        switch (spec.getKind()) {
            case TYPE_1:
                return new BossType1(spec.getX(), spec.getY(), spec.getLifePoints());
            case TYPE_2:
                return new BossType2(spec.getX(), spec.getY(), spec.getLifePoints());
            default:
                throw new IllegalArgumentException("Tipo de chefe desconhecido: " + spec.getKind());
        }
    }

    public PowerUp createPowerUp(PowerUpSpawnEntry spec) {
        switch (spec.getKind()) {
            case SHIELD:
                return new ShieldPowerUp(spec.getX(), spec.getY());
            case TRIPLE_SHOT:
                return new TripleShotPowerUp(spec.getX(), spec.getY());
            default:
                throw new IllegalArgumentException("Tipo de power-up desconhecido: " + spec.getKind());
        }
    }
}
