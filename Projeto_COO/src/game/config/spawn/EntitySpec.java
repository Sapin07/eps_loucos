package game.config.spawn;

public class EntitySpec {
    private final SpawnKind kind;
    private final int type;
    private final long whenMs;
    private final double x;
    private final double y;
    private final int lifePoints;

    public EntitySpec(SpawnKind kind, int type, long whenMs, double x, double y, int lifePoints){
        this.kind = kind;
        this.type = type;
        this.whenMs = whenMs;
        this.x = x;
        this.y = y;
        this.lifePoints = lifePoints;
    }

    public SpawnKind getKind(){
        return kind;
    }

    public int getType(){
        return type;
    }

    public long getWhenMs(){
        return whenMs;
    }

    public double getX(){
        return x;
    }

    public double getY(){
        return y;
    }

    public int getLifePoints(){
        return lifePoints;
    }

    @Override
    public String toString() {
        return kind + " tipo=" + type + " quando=" + whenMs + "ms pos=(" + x + "," + y + ")"
            + (kind == SpawnKind.BOSS ? " vida=" + lifePoints : "");
    }

}