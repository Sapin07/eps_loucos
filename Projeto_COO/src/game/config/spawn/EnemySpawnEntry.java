package game.config.spawn;

public class EnemySpawnEntry extends EntitySpec {
    private final EnemyKind kind;

    public EnemySpawnEntry(EnemyKind kind, long whenMs, double x, double y){
        super(whenMs, x, y);
        this.kind = kind;
    }

    public EnemyKind getKind(){
        return kind;
    }

    @Override
    public String toString() {
        return "ENEMY kind=" + kind + " quando=" + getWhenMs() + "ms pos=(" + getX() + "," + getY() + ")";
    }
}
