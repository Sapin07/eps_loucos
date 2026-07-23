package game;

import java.util.List;
import java.util.LinkedList;

import game.entities.Entity;
import lib.GameLib;

public class Game {
    // todo:
    // - collisor manager (I want to handle with it)
    // - spawner
    // - parser
    // - ** read settings and phase parser (priority)
    // - try/catchs (we need to adjust some routines to be more robust)
    // - a logger? import java.util.Logger (is useful for dev/debug time and a standard pattern on Any kind of projects. Check it ;))

    private GameState state;
    private long lastFrameTime;

    // imutable
    private final List<Entity> entities;

    public Game() {
        this.state = GameState.LOADING;
        this.entities = new LinkedList<>();
    }

    public void run(){
        GameLib.initGraphics();

        state = GameState.PLAYING;
        lastFrameTime = System.currentTimeMillis();

        // we can use a thread observer? check it 
        while (state == GameState.PLAYING || state == GameState.PHASE_TRANSITION) {
            long now = System.currentTimeMillis();
            long delta = now - lastFrameTime;
            lastFrameTime = now;

            if (GameLib.iskeyPressed(GameLib.KEY_ESCAPE)) {
                break;
            }

            update(delta);
            render();
        }
    }

    private void update(long delta) {
        for (Entity e: entities){
            e.update(delta);
        }

        entities.removeIf(e -> !e.isActive());
    }

    private void render() {
        for (Entity e : entities){
            e.draw();
        }

        GameLib.display();
    }
}
