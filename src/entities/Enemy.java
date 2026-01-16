package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * Base class for enemies in the game.
 *
 * Enemies patrol back and forth and can hurt the player.
 * Students: In Week 14, you'll create subclasses (Walker, Flyer) that extend this class!
 *
 * The 'protected' keyword means subclasses can access these fields directly.
 */
public class Enemy {

    // Position (protected so subclasses can access)
    protected double x;
    protected double y;

    // Velocity
    protected double velocityX;
    protected double velocityY;

    // Size
    protected int width;
    protected int height;

    // State
    protected boolean alive;
    protected int health;

    // Patrol bounds (where the enemy walks between)
    protected double patrolLeft;
    protected double patrolRight;

    // Visual
    protected Color color;

    /**
     * Create a new enemy at the specified position.
     * @param x Starting X position
     * @param y Starting Y position
     */
    public Enemy(double x, double y) {
        this.x = x;
        this.y = y;
        this.width = 40;
        this.height = 40;
        this.velocityX = 2; // Default patrol speed
        this.velocityY = 0;
        this.alive = true;
        this.health = 1;
        this.color = new Color(220, 60, 60); // Red

        // Default patrol range: 100 pixels in each direction
        this.patrolLeft = x - 100;
        this.patrolRight = x + 100;
    }

    /**
     * Create an enemy with custom patrol bounds.
     */
    public Enemy(double x, double y, double patrolLeft, double patrolRight) {
        this(x, y);
        this.patrolLeft = patrolLeft;
        this.patrolRight = patrolRight;
    }

    /**
     * Update the enemy each frame.
     * Called by the game loop.
     */
    public void update() {
        if (!alive) return;

        // Call the move method (can be overridden by subclasses)
        move();
    }

    /**
     * Move the enemy.
     *
     * DEFAULT BEHAVIOR: Patrol back and forth horizontally.
     *
     * WEEK 14: Override this method in subclasses to create different movement patterns!
     * - Walker: Could add gravity and platform awareness
     * - Flyer: Could move in a sine wave pattern
     */
    public void move() {
        // Move horizontally
        x += velocityX;

        // Reverse direction at patrol bounds
        if (x <= patrolLeft) {
            x = patrolLeft;
            velocityX = Math.abs(velocityX); // Move right
        } else if (x + width >= patrolRight) {
            x = patrolRight - width;
            velocityX = -Math.abs(velocityX); // Move left
        }
    }

    /**
     * Take damage.
     * @param amount Amount of damage
     */
    public void takeDamage(int amount) {
        health -= amount;
        if (health <= 0) {
            alive = false;
        }
    }

    /**
     * Called when the player collides with this enemy.
     * @param player The player that collided
     * @param fromAbove true if player is above enemy center (stomping)
     */
    public void onPlayerCollision(Player player, boolean fromAbove) {
        if (!alive) return;

        if (fromAbove) {
            // Player stomped on enemy
            takeDamage(1);
            player.bounce();
        } else {
            // Enemy hurts player
            player.takeDamage(1);
        }
    }

    /**
     * Get the collision bounds.
     * @return Rectangle representing the enemy's hitbox
     */
    public Rectangle getBounds() {
        return new Rectangle((int)x, (int)y, width, height);
    }

    /**
     * Draw the enemy.
     * @param g The graphics context
     */
    public void draw(Graphics g) {
        if (!alive) return;

        // Body
        g.setColor(color);
        g.fillRect((int)x, (int)y, width, height);

        // Outline
        g.setColor(color.darker());
        g.drawRect((int)x, (int)y, width, height);

        // Angry eyes
        g.setColor(Color.WHITE);
        int eyeY = (int)y + 8;
        g.fillOval((int)x + 6, eyeY, 10, 10);
        g.fillOval((int)x + width - 16, eyeY, 10, 10);

        // Pupils (looking in movement direction)
        g.setColor(Color.BLACK);
        int pupilOffset = velocityX > 0 ? 4 : 2;
        g.fillOval((int)x + 6 + pupilOffset, eyeY + 3, 5, 5);
        g.fillOval((int)x + width - 16 + pupilOffset, eyeY + 3, 5, 5);

        // Angry eyebrows
        g.setColor(Color.BLACK);
        if (velocityX > 0) {
            g.drawLine((int)x + 6, eyeY - 2, (int)x + 16, eyeY + 2);
            g.drawLine((int)x + width - 16, eyeY + 2, (int)x + width - 6, eyeY - 2);
        } else {
            g.drawLine((int)x + 6, eyeY + 2, (int)x + 16, eyeY - 2);
            g.drawLine((int)x + width - 16, eyeY - 2, (int)x + width - 6, eyeY + 2);
        }
    }

    // Getters
    public double getX() { return x; }
    public double getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
    public boolean isAlive() { return alive; }
    public int getHealth() { return health; }
}
