package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import game.GameConfig;

/**
 * A floor trap that damages the player.
 * Week 7: Methods (Writing)
 */
public class Trap {
    private int x, y, width, height;
    private int baseDamage;
    private GameConfig config;

    public Trap(int x, int y, int width, int height, int baseDamage, GameConfig config) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.baseDamage = baseDamage;
        this.config = config;
    }

    public void draw(Graphics g) {
        g.setColor(new Color(100, 100, 100)); // Stone color
        g.fillRect(x, y + height / 2, width, height / 2);

        // Spikes
        g.setColor(Color.LIGHT_GRAY);
        int spikeCount = width / 10;
        for (int i = 0; i < spikeCount; i++) {
            int[] px = { x + i * 10, x + i * 10 + 5, x + i * 10 + 10 };
            int[] py = { y + height / 2, y, y + height / 2 };
            g.fillPolygon(px, py, 3);
            g.setColor(Color.DARK_GRAY);
            g.drawPolygon(px, py, 3);
            g.setColor(Color.LIGHT_GRAY);
        }
    }

    public int getDamage(int armorPercent) {
        return config.calculateDamage(baseDamage, armorPercent);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}
