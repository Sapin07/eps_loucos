package game.core.collision;

import java.util.List;

import game.core.entities.OwnerType;
import game.core.entities.Player;
import game.core.entities.Projectile;
import game.core.entities.enemies.Boss;
import game.core.entities.enemies.Enemy;
import game.core.entities.powerUps.PowerUp;

public class CollisionManager {
    // the projectiles list are updated in human notice time, while the collisions are checked every frame. 
    // So maybe is a good idea to optimize the collisions checks with some ordered lists
    // maybe think about it is kinda overkill because there arent soooo many enemies and projectiles for now
    // but for hard core phases this can be a good performance boost (check after)
    // todo:
    // multi threaded approach to be analyzed

    public void checkCollisions(Player player, List<Enemy> enemies, List<Projectile> projectiles){
        checkProjectilesAgainstEnemies(projectiles, enemies);
        checkProjectilesAgainstPlayer(projectiles, player);
        checkEnemiesAgainstPlayer(enemies, player);
    }

    public void checkPowerUpPickup(Player player, List<PowerUp> powerUps){
        if (!player.isActive()) return;

        for (PowerUp p : powerUps){
            if (p.isActive() && p.hasCollidedWith(player)) {
                p.collectBy(player);
            }
        }
    }

    private void checkProjectilesAgainstEnemies(List<Projectile> projectiles, List<Enemy> enemies){
        for(Projectile p : projectiles){
            if (p.getOwner() != OwnerType.PLAYER || !p.isActive()){
                continue; // skip unecessary checks
            }

            for(Enemy e : enemies){
                if (e.isActive() && p.hasCollidedWith(e)) {
                    e.takeDamage(p.getDamage());
                    p.deadactivate();
                    break; // projectile already consumed no more collisions check
                }
            }
        }
    }

    private void checkProjectilesAgainstPlayer(List<Projectile> projectiles, Player player){
        for(Projectile p : projectiles){
            if (!player.isActive()) return;

            if (p.getOwner() == OwnerType.ENEMY && p.hasCollidedWith(player)) {
                player.takeDamage(p.getDamage());
                p.deadactivate();
            }
        }
    }

    private void checkEnemiesAgainstPlayer(List<Enemy> enemies, Player player){
        if(!player.isActive()) return;

        for(Enemy e : enemies){
            if (e.isActive() && e.hasCollidedWith(player)) {
                player.takeDamage(e.getContactDamage());
                e.takeDamage(e.getHealth()); // explodes the enemy
                break;
            }
        }
    }

}
