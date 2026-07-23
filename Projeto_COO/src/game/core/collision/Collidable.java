package game.core.collision;

public interface Collidable{
    // box collisor
    // X and Y for position and width and height for dimensions
    double getX();
    double getY();
    double getWidth();
    double getHeight();

    // Check collision based on position and type of object
    // e.g. case enemy collide with another enemy hasCollided -> false
    default boolean hasCollided(Collidable other){
        double halfW1 = getWidth() / 2.0;
        double halfH1 = getHeight() / 2.0;
        double halfW2 = other.getWidth() / 2.0;
        double halfH2 = other.getHeight() / 2.0;

        return Math.abs(getX() - other.getX()) < (halfW1 + halfW2)
            && Math.abs(getY() - other.getY()) < (halfH1 + halfH2);
    };
    // this must be overwritten based on each class
}