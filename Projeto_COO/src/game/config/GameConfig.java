package game.config;

import java.util.Collections;
import java.util.List;


public class GameConfig {
    private final int playerHealth;
    private final List<String> phaseFileNames; // immutable totally and completely never ever change it, or BOOOOOMM

    public GameConfig(int playerHealth, List<String> phaseFileNames){
        this.playerHealth = playerHealth;
        this.phaseFileNames = Collections.unmodifiableList(phaseFileNames);
    }

    public int getPlayerHealth() {
        return playerHealth;
    }

    public List<String> getPhaseFileNames() {
        return phaseFileNames;
    }
}
