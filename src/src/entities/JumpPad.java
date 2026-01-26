package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import game.GameConfig;

/**
 * A jump pad that boosts the player when touched.
 * Week 6: Methods (Calling)
 */
public class JumpPad {
    private int x, y, width, height;
    private Color color;
    private GameConfig config;

    public JumpPad(int x, int y, int width, int height, GameConfig config) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.config = config;
        this.color = new Color(255, 165, 0); // Orange
    }

    public void draw(Graphics g, int playerCoins) {
        boolean active = config.isJumpPadActive(playerCoins);

        // Shadow
        g.setColor(new Color(0, 0, 0, 50));
        g.fillRect(x + 4, y + 4, width, height);

        // Base
        if (active) {
            g.setColor(color);
            // Animated effect for active pad
            int pulse = (int) (Math.sin(System.currentTimeMillis() / 200.0) * 5);
            g.fillRect(x, y - (pulse > 0 ? pulse : 0), width, height + (pulse > 0 ? pulse : 0));
        } else {
            g.setColor(Color.GRAY);
            g.fillRect(x, y, width, height);
        }

        // Details
        g.setColor(Color.BLACK);
        g.drawRect(x, y, width, height);

        if (active) {
            g.setColor(Color.WHITE);
            g.drawString("BOOST", x + 5, y + 15);
        } else {
            g.setColor(Color.DARK_GRAY);
            g.drawString("OFF", x + 15, y + 15);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
