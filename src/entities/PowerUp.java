package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * A collectible power-up that grants the player temporary abilities.
 * Week 12: Objects & Classes (Creating)
 * 
 * Students: You must implement this class!
 */
public class PowerUp {
    // TODO: Add fields (type, duration, x, y, collected)

    // TEMPORARY FIELDS for compilation - Students should replace these
    private String type;
    private int x, y;
    private int duration = 300;
    private boolean collected = false;

    /**
     * Create a new PowerUp.
     */
    public PowerUp(String type, int x, int y) {
        this.type = type;
        this.x = x;
        this.y = y;
    }

    /**
     * Update the power-up each frame.
     */
    public void tick(Player player) {
        if (collected && duration > 0) {
            duration--;
        }
        if (duration <= 0) {
            if (type.equals("jump")) {
                player.setJumpMultiplier(1);
            }
            if (type.equals("speed")) {
                player.setMoveSpeedMultiplier(1);
            }
        }
    }

    /**
     * Apply the power-up effect to the player.
     */
    public void apply(Player player) {
        // TODO: Implement effects based on type
        // Review the Player class for available methods

    }

    public boolean isExpired() {
        return duration <= 0;
    }

    public void draw(Graphics g) {
        if (collected)
            return;

        g.setColor(Color.YELLOW);
        if (type.equals("speed"))
            g.setColor(Color.CYAN);
        if (type.equals("jump"))
            g.setColor(Color.GREEN);

        g.fillOval(x, y, 20, 20);
        g.setColor(Color.BLACK);
        g.drawOval(x, y, 20, 20);
        g.drawString(type.substring(0, 1).toUpperCase(), x + 6, y + 15);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, 20, 20);
    }

    public void setCollected(boolean collected) {
        this.collected = collected;
    }

    public boolean isCollected() {
        return collected;
    }

    public String getType() {
        return type;
    }
}
