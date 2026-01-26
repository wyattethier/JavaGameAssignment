package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * A collectible coin that increases the player's coin count.
 */
public class Coin {
    private int x, y;
    private int width = 16;
    private int height = 16;
    private boolean collected = false;

    public Coin(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw(Graphics g) {
        if (collected)
            return;

        // Gold color
        g.setColor(new Color(255, 215, 0));
        g.fillOval(x, y, width, height);

        // Shine
        g.setColor(new java.awt.Color(255, 255, 255, 150));
        g.fillOval(x + 3, y + 3, 5, 5);

        // Outline
        g.setColor(new Color(184, 134, 11));
        g.drawOval(x, y, width, height);
    }

    public boolean checkCollision(Rectangle playerBounds) {
        if (collected)
            return false;

        Rectangle coinBounds = new Rectangle(x, y, width, height);
        if (coinBounds.intersects(playerBounds)) {
            collected = true;
            return true;
        }
        return false;
    }

    public boolean isCollected() {
        return collected;
    }
}
