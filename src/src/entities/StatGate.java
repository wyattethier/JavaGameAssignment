package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import game.GameConfig;
import interfaces.Constants;

/**
 * A gate that only opens if the player has specific stats.
 * Week 11: Objects & Classes (Exploring)
 */
public class StatGate implements Constants {
    private int x, y, width, height;
    private GameConfig config;

    public StatGate(int x, int y, int width, int height, GameConfig config) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.config = config;
    }

    public void draw(Graphics g, Player player) {
        boolean canPass = config.canPassStatGate(player);

        // Frame
        g.setColor(new Color(153, 50, 204)); // Dark Orchid
        g.fillRect(x - 5, y - 5, width + 10, height + 10);

        if (canPass) {
            g.setColor(new Color(100, 50, 150, 100)); // Transparent purple
            g.fillRect(x, y, width, height);
            g.setColor(Color.BLACK);
            g.drawString("PASS", x - 5, y - 10);
        } else {
            g.setColor(new Color(40, 0, 40));
            g.fillRect(x, y, width, height);

            // Bars
            g.setColor(Color.BLACK);
            for (int i = 0; i < width; i += 15) {
                g.fillRect(x + i, y, 5, height);
            }

            g.setColor(Color.RED);
            g.drawString("LOCKED", x - 5, y - 10);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}
