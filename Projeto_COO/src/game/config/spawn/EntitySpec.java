package game.config.spawn;

public abstract class EntitySpec {
    private final long whenMs;
    private final double x;
    private final double y;

    protected EntitySpec(long whenMs, double x, double y){
        this.whenMs = whenMs;
        this.x = x;
        this.y = y;
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
}
