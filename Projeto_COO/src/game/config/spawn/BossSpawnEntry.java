package game.config.spawn;

public class BossSpawnEntry extends EntitySpec {
    private final BossKind kind;
    private final int lifePoints;

    public BossSpawnEntry(BossKind kind, int lifePoints, long whenMs, double x, double y){
        super(whenMs, x, y);
        this.kind = kind;
        this.lifePoints = lifePoints;
    }

    public BossKind getKind(){
        return kind;
    }

    public int getLifePoints(){
        return lifePoints;
    }

    @Override
    public String toString() {
        return "BOSS kind=" + kind + " vida=" + lifePoints + " quando=" + getWhenMs() + "ms pos=(" + getX() + "," + getY() + ")";
    }
}
