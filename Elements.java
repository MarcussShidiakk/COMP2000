import java.awt.Color;

public class Elements {
    boolean moveable;
    boolean canBurn;
    Color color;
    boolean reverseGravity;

    public Elements(boolean moveable, boolean canBurn, Color color, boolean reverseGravity) {
        this.moveable = moveable;
        this.canBurn = canBurn;
        this.color = color;
        this.reverseGravity = reverseGravity;
    }

    public Color getColor() {
        return color;
    }
}

class Sand extends Elements {
    public Sand() {
        super(true, false, Color.YELLOW, false);
    }
}

class Water extends Elements {
    public Water() {
        super(true, false, Color.BLUE, false);
    }
}

class Fire extends Elements {

    private static final Color[] FLAMES = {
        new Color(255, 240, 180), // white hot
        new Color(255, 190, 60),  // yellow
        new Color(255, 140, 20),  // orange
        new Color(230, 80, 10),   // deep orange
        new Color(180, 35, 0)     // red
    };

    private static final long LIFETIME = 3400;

    private final long bornAt = System.currentTimeMillis();

    public Fire() {
        // reverseGravity = true -> fire rises like a real flame instead of piling up like sand
        super(false, true, Color.ORANGE, true);
    }
    public boolean isDead() {
        return System.currentTimeMillis() - bornAt >= LIFETIME;
    }
    @Override
    public Color getColor() {
        long age = System.currentTimeMillis() - bornAt;

        if (age >= LIFETIME) {
            return Color.DARK_GRAY;
        }

        Color flame = FLAMES[(int) (Math.random() * FLAMES.length)];

        // fade out over the last second of its life
        if (age > LIFETIME - 1000) {
            float f = (age - (LIFETIME - 1000)) / 1000f;
            return new Color(
                (int) (flame.getRed() + f * (64 - flame.getRed())),
                (int) (flame.getGreen() + f * (64 - flame.getGreen())),
                (int) (flame.getBlue() + f * (64 - flame.getBlue())));
        }

        return flame;
    }
}

class Gas extends Elements {
    public Gas() {
        super(false, true, Color.WHITE, true);
    }
}