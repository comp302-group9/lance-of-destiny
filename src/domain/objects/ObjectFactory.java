package domain.objects;

import domain.objects.Barrier.*;

public class ObjectFactory {
    // Singleton instance
    private static ObjectFactory instance;

    // Private constructor to prevent instantiation
    private ObjectFactory() {}

    // Method to get the single instance of the factory
    public static synchronized ObjectFactory getInstance() {
        if (instance == null) {
            instance = new ObjectFactory();
        }
        return instance;
    }

    // Method to create a Paddle
    public Paddle createPaddle(int x, int y, int width, int height) {
        return new Paddle(x, y, width, height);
    }

    // Method to create a Fireball
    public Fireball createFireball(int x, int y, int width, int height) {
        return new Fireball(x, y, width, height);
    }

    // Method to create a Barrier based on type
    public Barrier createBarrier(int type, int x, int y) {
        switch (type) {
            case 1:
                return new SimpleBarrier(x, y);
            case 2:
                return new ReinforcedBarrier(x, y);
            case 3:
                return new ExplosiveBarrier(x, y);
            case 4:
                return new RewardingBarrier(x, y);
            default:
                return null;
        }
    }
}
