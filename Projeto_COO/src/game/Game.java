package game;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import game.config.GameConfig;
import game.config.phase.Phase;
import game.config.phase.PhaseConfigParser;
import game.config.spawn.EntitySpec;
import game.core.collision.CollisionManager;
import game.core.entities.Player;
import game.core.entities.Projectile;
import game.core.entities.enemies.Boss;
import game.core.entities.enemies.Enemy;
import game.core.entities.powerUps.PowerUp;
import game.core.spawn.SpawnManager;
import lib.GameLib;

public class Game {
    // todo:
    // - should we use multi threads?
    // - fix boss
    // - hud

    private static final Logger LOGGER = Logger.getLogger(Game.class.getName());

    private final GameConfig config;
    private final SpawnManager spawnManager;
    private final CollisionManager collisionMan;

    private GameState state;
    private long lastFrameTime;

    private Player player;
    private final List<Enemy> enemies;
    private final List<Projectile> projectiles;
    private final List<PowerUp> powerUps;

    private int currentPhaseIndex;
    private Phase currentPhase;
    private long phaseElapsedMs;

    public Game(GameConfig config) {
        this.config = config;
        this.state = GameState.LOADING;
        this.spawnManager = new SpawnManager();
        this.collisionMan = new CollisionManager();
        this.enemies = new LinkedList<>();
        this.projectiles = new LinkedList<>();
        this.powerUps = new LinkedList<>();
    }

    public void run(){
        try {
            GameLib.initGraphics_SAFE_MODE();

            player = new Player(GameLib.WIDTH / 2.0, GameLib.HEIGHT - 60, config.getPlayerHealth());
            currentPhaseIndex = 0;
            loadPhase(currentPhaseIndex);

            state = GameState.PLAYING;
            lastFrameTime = System.currentTimeMillis();
            LOGGER.info(() -> "Game started: " + config.getPhaseFileNames().size() + " phase(s), player health=" + config.getPlayerHealth());

            // we can use a thread observer? check it
            while (state == GameState.PLAYING || state == GameState.PHASE_TRANSITION) {
                long now = System.currentTimeMillis();
                long delta = now - lastFrameTime;
                lastFrameTime = now;

                if (GameLib.iskeyPressed(GameLib.KEY_ESCAPE) || !player.isActive()) {
                    LOGGER.info("Player defeated or quit, ending game.");
                    state = GameState.GAME_OVER;
                    break;
                }

                update(delta);
                render();
            }
        } catch (Exception e) {
            // last resort
            // without this, a startup/runtime failure (e.g. a phase file not found because of the working directory)
            // would crash the thread after initGraphics() already opened the window, leaving a frozen (inside like the music, wake me up, wake me up inside),
            //  never-drawn screen with no visible clue why
            LOGGER.log(Level.SEVERE, "Fatal error, aborting game.", e);
            state = GameState.GAME_OVER;
        }

        announceOutcome();
    }

    private void loadPhase(int index) {
        String phaseFile = config.getPhaseFileNames().get(index);
        LOGGER.info(() -> "Loading phase " + (index + 1) + "/" + config.getPhaseFileNames().size() + " from \"" + phaseFile + "\"");

        try {
            currentPhase = new PhaseConfigParser().parser(phaseFile);
        } catch (IOException e) {
            throw new IllegalStateException("Não foi possível carregar a fase \"" + phaseFile + "\".", e);
        }

        phaseElapsedMs = 0;
    }

    private void update(long delta) {
        // other good point fot multi threaded approach
        phaseElapsedMs += delta;

        player.update(delta);
        spawnIfPresent(player.tryShoot());

        spawnDueEntities();

        for (Enemy e : enemies){
            e.update(delta);
            spawnIfPresent(e.tryShoot(System.currentTimeMillis(), player.getX(), player.getY()));
        }

        for (PowerUp p : powerUps){
            p.update(delta);
        }

        for (Projectile p : projectiles){
            p.update(delta);
        }

        collisionMan.checkCollisions(player, enemies, projectiles);
        collisionMan.checkPowerUpPickup(player, powerUps);

        enemies.removeIf(e -> !e.isActive());
        projectiles.removeIf(p -> !p.isActive());
        powerUps.removeIf(p -> !p.isActive());

        if (currentPhase.isComplete()) {
            LOGGER.info(() -> "Phase " + (currentPhaseIndex + 1) + " boss defeated.");
            advancePhase();
        }
    }

    private void spawnDueEntities() {
        List<EntitySpec> due = currentPhase.pollDueSpawns(phaseElapsedMs);

        for (EntitySpec spec : due) {
            try {
                spawnEntity(spec);
            } catch (IllegalArgumentException e) {
                // a single malformed/unknown entry in the phase file shouldn't take the whole game down; skip it and keep playing.
                LOGGER.log(Level.WARNING, "Skipping invalid spawn entry: " + spec, e);
            }
        }
    }

    private void spawnEntity(EntitySpec spec) {
        switch (spec.getKind()) {
            case ENEMY:
                enemies.add(spawnManager.createEnemy(spec));
                LOGGER.fine(() -> "Spawned enemy: " + spec);
                break;
            case BOSS:
                Boss boss = spawnManager.createBoss(spec);
                boss.setTarget(player);
                currentPhase.setBossEntity(boss);
                enemies.add(boss);
                LOGGER.info(() -> "Spawned boss: " + spec);
                break;
            case POWERUP:
                powerUps.add(spawnManager.createPowerUp(spec));
                LOGGER.fine(() -> "Spawned power-up: " + spec);
                break;
        }
    }

    private void advancePhase() {
        int nextIndex = currentPhaseIndex + 1;

        if (nextIndex >= config.getPhaseFileNames().size()) {
            LOGGER.info("All phases cleared, victory!");
            state = GameState.VICTORY;
            return;
        }

        state = GameState.PHASE_TRANSITION;
        currentPhaseIndex = nextIndex;
        loadPhase(currentPhaseIndex);
        state = GameState.PLAYING;
    }

    private void spawnIfPresent(Projectile p){
        if (p != null)
            projectiles.add(p);
    }

    private void spawnIfPresent(List<Projectile> newProjectiles){
        if (newProjectiles != null)
            projectiles.addAll(newProjectiles);
    }

    private void render() {
        // multi threaded is good
        player.draw();

        for(Enemy e : enemies){
            e.draw();
        }

        for(PowerUp p : powerUps){
            p.draw();
        }

        for(Projectile p : projectiles){
            p.draw();
        }

        GameLib.display();
    }

    private void announceOutcome() {
        if (state == GameState.VICTORY) {
            LOGGER.info("Vitória! Você derrotou todos os chefes.");
        } else if (state == GameState.GAME_OVER) {
            LOGGER.info("Game Over.");
        }
    }
}
