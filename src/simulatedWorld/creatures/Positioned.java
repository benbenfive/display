package simulatedWorld.creatures;

public class Positioned {
    protected double x, y;

    public Positioned(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
