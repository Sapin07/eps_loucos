package game.config.spawn;

public class PowerUpSpawnEntry extends EntitySpec {
    private final PowerUpKind kind;

    public PowerUpSpawnEntry(PowerUpKind kind, long whenMs, double x, double y){
        super(whenMs, x, y);
        this.kind = kind;
    }

    public PowerUpKind getKind(){
        return kind;
    }

    @Override
    public String toString() {
        return "POWERUP kind=" + kind + " quando=" + getWhenMs() + "ms pos=(" + getX() + "," + getY() + ")";
    }
}
