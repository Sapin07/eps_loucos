package game;

import java.util.List;
import java.util.LinkedList;

import game.core.collision.CollisionManager;
import game.core.entities.Enemy;
import game.core.entities.Player;
import game.core.entities.Projectile;
import game.core.entities.enemies.EnemyType1;
import game.core.entities.enemies.EnemyType2;
import lib.GameLib;

public class Game {
    // todo:
    // - spawner
    // - parser
    // - ** read settings and phase parser (priority)
    // - try/catchs (we need to adjust some routines to be more robust)
    // - a logger? import java.util.Logger (is useful for dev/debug time and a standard pattern on Any kind of projects. Check it ;))

    private GameState state;
    private long lastFrameTime;

    private Player player;
    // imutable
    private final List<Enemy> enemies;
    private final List<Projectile> projectiles;
    private final CollisionManager collisionMan;

    public Game() {
        this.state = GameState.LOADING;
        this.enemies = new LinkedList<>();
        this.projectiles = new LinkedList<>();
        this.collisionMan = new CollisionManager();
    }

    public void run(){
        GameLib.initGraphics();

        player = new Player(GameLib.WIDTH/ 2.0, GameLib.HEIGHT - 60, 3); // just a example, a config file is better

        // hard coded for now, we need a Spawn manager
        enemies.add(new EnemyType1(100, -20));
        enemies.add(new EnemyType2(380, -80));

        state = GameState.PLAYING;
        lastFrameTime = System.currentTimeMillis();

        // we can use a thread observer? check it 
        while (state == GameState.PLAYING || state == GameState.PHASE_TRANSITION) {
            long now = System.currentTimeMillis();
            long delta = now - lastFrameTime;
            lastFrameTime = now;

            if (GameLib.iskeyPressed(GameLib.KEY_ESCAPE) || !player.isActive()) {
                state = GameState.GAME_OVER;
                break;
            }

            update(delta);
            render();
        }
    }

    private void update(long delta) {
        // other good point fot multi threaded approach
        player.update(delta);

        for (Enemy e : enemies){
            e.update(delta);
            Projectile p = e.tryShoot(System.currentTimeMillis(), player.getX(), player.getY());
            spawnIfPresent(p);
        }

        for (Projectile p : projectiles){
            p.update(delta);
        }

        collisionMan.checkCollisions(player, enemies, projectiles);

        enemies.removeIf(e -> !e.isActive());
        projectiles.removeIf(p -> !p.isActive());
    }

    private void spawnIfPresent(Projectile p){
        if (p != null)
            projectiles.add(p);
    }

    private void render() {
        // multi threaded is good
        player.draw();

        for(Enemy e : enemies){
            e.draw();
        }

        for(Projectile p : projectiles){
            p.draw();
        }

        GameLib.display();
    }
}
