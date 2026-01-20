package entities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import game.GameConfig;

import interfaces.Constants;

/**
 * A clickable button in the game world.
 * Used in Week 4 to teach if-statements and mouse interaction.
 */
public class LevelButton implements Constants {

    private int x, y, width, height;
    private int clickCount;
    private Color color;
    private GameConfig config;

    /**
     * Create a new button.
     * 
     * @param x      X position
     * @param y      Y position
     * @param width  Width
     * @param height Height
     */
    public LevelButton(int x, int y, int width, int height, GameConfig config) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.clickCount = 0;
        this.color = new Color(200, 50, 50); // Default red
        this.config = config;
    }

    /**
     * Check if a click hit the button.
     * 
     * @param mouseX Click X
     * @param mouseY Click Y
     * @return true if button was clicked
     */
    public boolean handleClick(int mouseX, int mouseY) {
        Rectangle bounds = new Rectangle(x, y, width, height);
        if (bounds.contains(mouseX, mouseY)) {
            clickCount++;
            return true;
        }
        return false;
    }

    /**
     * Draw the button.
     */
    public void draw(Graphics g) {
        // Shadow
        g.setColor(new Color(0, 0, 0, 50));
        g.fillRect(x + 4, y + 4, width, height);

        // Button base
        if (config.isGoalFixed(clickCount)) {
            g.setColor(new Color(50, 200, 50)); // Green when done
        } else {
            g.setColor(color);
        }

        g.fillRect(x, y, width, height);

        // Border
        g.setColor(Color.BLACK);
        g.drawRect(x, y, width, height);

        // Label
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 12));
        String text = clickCount + " / " + REQUIRED_BUTTON_CLICKS;
        int textWidth = g.getFontMetrics().stringWidth(text);
        g.drawString(text, x + (width - textWidth) / 2, y + height / 2 + 5);

        g.setFont(new Font("Arial", Font.PLAIN, 10));
        String label = "CLICK ME";
        int labelWidth = g.getFontMetrics().stringWidth(label);
        g.drawString(label, x + (width - labelWidth) / 2, y - 5);
    }

    // Getters
    public int getClickCount() {
        return clickCount;
    }

    public boolean isActivated() {
        return clickCount >= REQUIRED_BUTTON_CLICKS;
    }

    public void reset() {
        clickCount = 0;
    }
}
