package world;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import interfaces.Constants;
import entities.Player;

/**
 * Represents a platform in the game world.
 *
 * Platforms are solid surfaces that the player can stand on.
 * Students: You'll explore this class in Week 11.
 */
public class Platform implements Constants {

    private int x;
    private int y;
    private int width;
    private int height;
    private Color color;
    private boolean moveable;
    private int maxHeight;

    /**
     * Create a new platform.
     * 
     * @param x      Left edge position
     * @param y      Top edge position
     * @param width  Width of the platform
     * @param height Height of the platform
     */
    public Platform(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.color = new Color(139, 90, 43); // Brown color
        this.moveable = false;
        this.maxHeight = -1; // -1 means no height limit
    }

    /**
     * Create a platform with a custom color.
     */
    public Platform(int x, int y, int width, int height, Color color) {
        this(x, y, width, height);
        this.color = color;
        this.moveable = false;
    }

    /**
     * Create a platform with a custom color and moveable property.
     */
    public Platform(int x, int y, int width, int height, Color color, boolean moveable) {
        this(x, y, width, height, color);
        this.moveable = moveable;
    }

    /**
     * Create a platform with all properties.
     */
    public Platform(int x, int y, int width, int height, Color color, boolean moveable, int maxHeight) {
        this(x, y, width, height, color, moveable);
        this.maxHeight = maxHeight;
    }

    /**
     * Get the collision bounds of this platform.
     * 
     * @return Rectangle representing the platform's hitbox
     */
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    /**
     * Draw the platform.
     * 
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

    public boolean checkCollision(Player player) {
        return player.getBounds().intersects(getBounds());
    }

    // Getters
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public boolean isMoveable() {
        return moveable;
    }

    public int getMaxHeight() {
        return maxHeight;
    }

    public void setY(int y) {
        this.y = y;
    }
}
