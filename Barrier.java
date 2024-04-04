
public abstract class Barrier {

	protected int x;
    protected int y;

    // Constructor
    public Barrier(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Method to handle the barrier being hit by the Fire Ball
    public abstract void onHit();

    // Getter methods
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

}
