package game.config.phase;

import java.util.List;
import java.util.ArrayList;

import game.config.spawn.EntitySpec;
import game.core.entities.enemies.Boss;


public class Phase {
    private final List<EntitySpec> schedule; // ordered by whenMs
    private int nextIndex = 0;
    private Boss bossEntity;

    public Phase(List<EntitySpec> schedule){
        this.schedule = schedule;
    }

    public List<EntitySpec> pollDueSpawns(long elapsedMs){
        // return the list of Entities to be consumed, based on Elapsed time and the spawnMS
        List<EntitySpec> due = new ArrayList<>();

        while (nextIndex < schedule.size() && schedule.get(nextIndex).getWhenMs() <= elapsedMs) {
            due.add(schedule.get(nextIndex));
            nextIndex++;
        }

        return due;
    }

    public void setBossEntity(Boss boss) {
        this.bossEntity = boss;
    }

    public Boss getBossEntity() {
        return bossEntity;
    }
    
    public boolean isComplete() {
        return bossEntity != null && !bossEntity.isActive();
    }
}
