package game;

import java.awt.Color;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import game.config.GameConfig;
import game.config.phase.Phase;
import game.config.phase.PhaseConfigParser;
import game.config.spawn.BossSpawnEntry;
import game.config.spawn.EnemySpawnEntry;
import game.config.spawn.EntitySpec;
import game.config.spawn.PowerUpSpawnEntry;
import game.config.spawn.SpawnManager;
import game.core.collision.CollisionManager;
import game.core.entities.Player;
import game.core.entities.Projectile;
import game.core.entities.enemies.Boss;
import game.core.entities.enemies.Enemy;
import game.core.entities.powerUps.PowerUp;
import game.ui.HUD;
import game.ui.StarField;
import game.ui.TextRenderer;
import lib.GameLib;

public class Game {
    // todo:
    // - should we use multi threads? no time for it but check it after

    private static final Logger LOGGER = Logger.getLogger(Game.class.getName());

    private static final long PHASE_TRANSITION_DELAY_MS = 2000;
    private static final long END_SCREEN_INPUT_DEBOUNCE_MS = 500;
    private static final long END_SCREEN_POLL_INTERVAL_MS = 16;

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
    private int completedPhaseIndex;
    private Phase currentPhase;
    private long phaseElapsedMs;
    private long phaseTransitionElapsedMs;

    private final StarField starField = new StarField();
    private final HUD hud;

    public Game(GameConfig config) {
        this.config = config;
        this.state = GameState.LOADING;
        this.spawnManager = new SpawnManager();
        this.collisionMan = new CollisionManager();
        this.enemies = new LinkedList<>();
        this.projectiles = new LinkedList<>();
        this.powerUps = new LinkedList<>();
        this.hud = new HUD(config.getPlayerHealth());
    }

    public void run(){
        try {
            GameLib.initGraphics_SAFE_MODE();

            boolean playAgain = true;

            while (playAgain) {
                startNewRound();
                playRound();

                if (state == GameState.QUIT) {
                    break;
                }

                playAgain = showEndScreenAndWaitForChoice();
                announceOutcome();
            }
        } catch (Exception e) {
            // last resort
            // without this, a startup/runtime failure (e.g. a phase file not found because of the working directory)
            // would crash the thread after initGraphics() already opened the window, leaving a frozen (inside like the music, wake me up, wake me up inside),
            //  never-drawn screen with no visible clue why
            LOGGER.log(Level.SEVERE, "Fatal error, aborting game.", e);
        }

        System.exit(0); // adicionei para sair quando clica ESC
    }

    private void startNewRound() {
        player = new Player(GameLib.WIDTH / 2.0, GameLib.HEIGHT - 60, config.getPlayerHealth());
        enemies.clear();
        projectiles.clear();
        powerUps.clear();

        currentPhaseIndex = 0;
        completedPhaseIndex = -1;
        loadPhase(currentPhaseIndex);

        state = GameState.PLAYING;
        lastFrameTime = System.currentTimeMillis();
        LOGGER.info(() -> "Round started: " + config.getPhaseFileNames().size() + " phase(s), player health=" + config.getPlayerHealth());
    }

    private void playRound() {
        // we can use a thread observer? check it
        while (state == GameState.PLAYING || state == GameState.PHASE_TRANSITION) {
            long now = System.currentTimeMillis();
            long delta = now - lastFrameTime;
            lastFrameTime = now;

            if (GameLib.iskeyPressed(GameLib.KEY_ESCAPE)) {
                LOGGER.info("Player quit.");
                state = GameState.QUIT;
                return;
            }

            if (!player.isActive()) {
                LOGGER.info("Player defeated.");
                state = GameState.GAME_OVER;
                return;
            }

            update(delta);
            render();
        }
    }

    private void loadPhase(int index) {
        String phaseFile = config.getPhaseFileNames().get(index);
        LOGGER.info(() -> "Loading phase " + (index + 1) + "/" + config.getPhaseFileNames().size() + " from \"" + phaseFile + "\"");

        try {
            currentPhase = new PhaseConfigParser().parse(phaseFile);
        } catch (IOException e) {
            throw new IllegalStateException("Não foi possível carregar a fase \"" + phaseFile + "\".", e);
        }

        phaseElapsedMs = 0;
    }

    private void update(long delta) {
        if (state == GameState.PHASE_TRANSITION) {
            phaseTransitionElapsedMs += delta;

            if (phaseTransitionElapsedMs >= PHASE_TRANSITION_DELAY_MS) {
                proceedToNextPhaseOrVictory();
            }

            return;
        }

        starField.update(delta);
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
            completedPhaseIndex = currentPhaseIndex;
            phaseTransitionElapsedMs = 0;
            state = GameState.PHASE_TRANSITION;
        }
    }

    private void proceedToNextPhaseOrVictory() {
        int nextIndex = currentPhaseIndex + 1;

        if (nextIndex >= config.getPhaseFileNames().size()) {
            LOGGER.info("All phases cleared, victory!");
            state = GameState.VICTORY;
            return;
        }

        currentPhaseIndex = nextIndex;
        loadPhase(currentPhaseIndex);
        state = GameState.PLAYING;
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
        if (spec instanceof EnemySpawnEntry enemySpec) {
            enemies.add(spawnManager.createEnemy(enemySpec));
            LOGGER.fine(() -> "Spawned enemy: " + spec);
        } else if (spec instanceof BossSpawnEntry bossSpec) {
            Boss boss = spawnManager.createBoss(bossSpec);
            boss.setTarget(player);
            currentPhase.setBossEntity(boss);
            enemies.add(boss);
            LOGGER.info(() -> "Spawned boss: " + spec);
        } else if (spec instanceof PowerUpSpawnEntry powerUpSpec) {
            powerUps.add(spawnManager.createPowerUp(powerUpSpec));
            LOGGER.fine(() -> "Spawned power-up: " + spec);
        } else {
            throw new IllegalArgumentException("Tipo de EntitySpec desconhecido: " + spec.getClass());
        }
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
        starField.draw();
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

        hud.draw(player, currentPhase.getBossEntity());

        if (state == GameState.PHASE_TRANSITION) {
            String message = "PHASE " + (completedPhaseIndex + 1) + " COMPLETE";
            TextRenderer.drawTextCentered(message, GameLib.WIDTH / 2.0, GameLib.HEIGHT / 2.0, Color.WHITE, 28);
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

    private boolean showEndScreenAndWaitForChoice() throws InterruptedException {
        boolean victory = state == GameState.VICTORY;
        long acceptInputAfter = System.currentTimeMillis() + END_SCREEN_INPUT_DEBOUNCE_MS;

        while (true) {
            renderEndScreen(victory);

            if (System.currentTimeMillis() >= acceptInputAfter) {
                if (GameLib.iskeyPressed(GameLib.KEY_ESCAPE)) {
                    return false;
                }
                if (GameLib.iskeyPressed(GameLib.KEY_CONTROL)) {
                    return true;
                }
            }

            Thread.sleep(END_SCREEN_POLL_INTERVAL_MS);
        }
    }

    private void renderEndScreen(boolean victory) {
        starField.draw();

        String title = victory ? "VICTORY!" : "GAME OVER";
        Color titleColor = victory ? Color.GREEN : Color.RED;
        TextRenderer.drawTextCentered(title, GameLib.WIDTH / 2.0, GameLib.HEIGHT / 2.0 - 60, titleColor, 44);
        TextRenderer.drawTextCentered("CONTROL TO PLAY AGAIN", GameLib.WIDTH / 2.0, GameLib.HEIGHT / 2.0 + 30, Color.WHITE, 20);

        GameLib.display();
    }
}
