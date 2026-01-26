package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import game.GameConfig;
import interfaces.Constants;

/**
 * A door that only opens once a certain score is reached.
 * Week 7: Methods (Writing)
 */
public class ScoringDoor implements Constants {
    private int x, y, width, height;
    private GameConfig config;

    public ScoringDoor(int x, int y, int width, int height, GameConfig config) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.config = config;
    }

    public void draw(Graphics g, int coins, int timeBonus, double comboMultiplier) {
        int currentScore = config.calculateScore(coins, timeBonus, comboMultiplier);
        boolean isOpen = currentScore >= REQUIRED_SCORE;

        // Frame
        g.setColor(new Color(101, 67, 33)); // Dark brown
        g.fillRect(x - 5, y - 5, width + 10, height + 10);

        if (isOpen) {
            // Open door (see through)
            g.setColor(new Color(50, 50, 50));
            g.fillRect(x, y, width, height);

            // "OPEN" text
            g.setColor(Color.GREEN);
            g.drawString("OPEN", x + 5, y + height / 2);
        } else {
            // Closed door
            g.setColor(new Color(150, 75, 0));
            g.fillRect(x, y, width, height);

            // Door details
            g.setColor(Color.BLACK);
            g.drawRect(x, y, width, height);
            g.fillOval(x + width - 15, y + height / 2, 8, 8); // handle

            // Requirement text
            g.setColor(Color.WHITE);
            g.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 10));
            g.drawString("SCORE: " + currentScore, x - 10, y - 10);
            g.drawString("/ " + REQUIRED_SCORE, x + 5, y + height + 15);
        }
    }

    public boolean isOpen(int coins, int timeBonus, double comboMultiplier) {
        return config.calculateScore(coins, timeBonus, comboMultiplier) >= REQUIRED_SCORE;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}
