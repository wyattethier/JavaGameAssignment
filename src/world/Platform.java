package world;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * Represents a platform in the game world.
 *
 * Platforms are solid surfaces that the player can stand on.
 * Students: You'll explore this class in Week 11.
 */
public class Platform {

    private int x;
    private int y;
    private int width;
    private int height;
    private Color color;

    /**
     * Create a new platform.
     * @param x Left edge position
     * @param y Top edge position
     * @param width Width of the platform
     * @param height Height of the platform
     */
    public Platform(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = new Color(139, 90, 43); // Brown color
    }

    /**
     * Create a platform with a custom color.
     */
    public Platform(int x, int y, int width, int height, Color color) {
        this(x, y, width, height);
        this.color = color;
    }

    /**
     * Get the collision bounds of this platform.
     * @return Rectangle representing the platform's hitbox
     */
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    /**
     * Draw the platform.
     * @param g The graphics context
     */
    public void draw(Graphics g) {
        // Main platform body
        g.setColor(color);
        g.fillRect(x, y, width, height);

        // Top edge highlight
        g.setColor(color.brighter());
        g.fillRect(x, y, width, 3);

        // Border
        g.setColor(color.darker());
        g.drawRect(x, y, width, height);
    }

    // Getters
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
}
