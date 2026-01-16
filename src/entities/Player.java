package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import util.InputHandler;
import game.GameConfig;
import world.Platform;

/**
 * The player character in the game.
 *
 * Handles movement, jumping, and collision with platforms.
 * Students: You'll explore this class in Week 11 and add new features in Week
 * 12.
 */
public class Player {

    // Position (using doubles for smooth movement)
    private double x;
    private double y;

    // Velocity
    private double velocityX;
    private double velocityY;

    // Size
    private int width;
    private int height;

    // State
    private boolean onGround;
    private boolean hasDoubleJumped;
    private int health;
    private int maxHealth;
    private int coins;

    // Coyote time - allows jumping shortly after leaving a platform
    private int coyoteTimer = 0;
    private final int COYOTE_TIME = 8; // frames of grace period after leaving ground

    // Reference to game config for physics values
    private GameConfig config;

    // Visual
    private Color color;

    /**
     * Create a new player.
     * 
     * @param x      Starting X position
     * @param y      Starting Y position
     * @param config Game configuration for physics values
     */
    public Player(double x, double y, GameConfig config) {
        this.x = x;
        this.y = y;
        this.config = config;
        this.width = config.playerWidth;
        this.height = config.playerHeight;
        this.velocityX = 0;
        this.velocityY = 0;
        this.onGround = false;
        this.hasDoubleJumped = false;
        this.coyoteTimer = 0;
        this.health = 3;
        this.maxHealth = 3;
        this.coins = 0;
        this.color = new Color(65, 105, 225); // Royal blue
    }

    /**
     * Update the player each frame.
     * 
     * @param input     The input handler for keyboard state
     * @param platforms Array of platforms to collide with
     */
    public void update(InputHandler input, Platform[] platforms) {
        // Update coyote timer (counts down when not on ground)
        if (!onGround && coyoteTimer > 0) {
            coyoteTimer--;
        }

        // Handle horizontal movement
        handleInput(input);

        // Apply gravity
        velocityY += config.gravity;

        // Move horizontally and check collisions
        x += velocityX;
        handleHorizontalCollisions(platforms);

        // Move vertically and check collisions
        y += velocityY;
        handleVerticalCollisions(platforms);

        // Keep player in bounds (left and right edges)
        if (x < 0)
            x = 0;
        if (x + width > config.windowWidth)
            x = config.windowWidth - width;
    }

    /**
     * Handle keyboard input for movement and jumping.
     */
    private void handleInput(InputHandler input) {
        // Horizontal movement
        if (input.isKeyPressed(KeyEvent.VK_LEFT) || input.isKeyPressed(KeyEvent.VK_A)) {
            velocityX = -config.moveSpeed;
        } else if (input.isKeyPressed(KeyEvent.VK_RIGHT) || input.isKeyPressed(KeyEvent.VK_D)) {
            velocityX = config.moveSpeed;
        } else {
            velocityX = 0;
        }

        // Jumping (with coyote time support)
        if (input.isKeyJustPressed(KeyEvent.VK_SPACE) || input.isKeyJustPressed(KeyEvent.VK_W)
                || input.isKeyJustPressed(KeyEvent.VK_UP)) {
            if (onGround || coyoteTimer > 0) {
                // Regular jump (includes coyote time window)
                velocityY = -config.jumpStrength;
                onGround = false;
                coyoteTimer = 0; // Consume the coyote time
                hasDoubleJumped = false;
            } else if (config.canDoubleJump && !hasDoubleJumped) {
                // Double jump (Week 4)
                velocityY = -config.jumpStrength * 0.85;
                hasDoubleJumped = true;
            }
        }
    }

    /**
     * Handle collisions with platforms in the horizontal direction.
     */
    private void handleHorizontalCollisions(Platform[] platforms) {
        Rectangle playerBounds = getBounds();

        for (Platform platform : platforms) {
            if (platform == null)
                continue;

            Rectangle platBounds = platform.getBounds();
            if (playerBounds.intersects(platBounds)) {
                // Collision detected - push player out

                if (velocityX > 0) {
                    // Moving right, push left
                    x = platform.getX() - width;
                } else if (velocityX < 0) {
                    // Moving left, push right
                    x = platform.getX() + platform.getWidth();
                }

                velocityX = 0;
                playerBounds = getBounds(); // Update bounds after correction
            }
        }
    }

    /**
     * Handle collisions with platforms in the vertical direction.
     */
    private void handleVerticalCollisions(Platform[] platforms) {
        Rectangle playerBounds = getBounds();
        onGround = false;

        for (Platform platform : platforms) {
            if (platform == null)
                continue;

            Rectangle platBounds = platform.getBounds();
            if (playerBounds.intersects(platBounds)) {
                // Collision detected

                if (velocityY > 0) {
                    // Falling down, land on platform
                    y = platform.getY() - height;
                    velocityY = 0;
                    onGround = true;
                    coyoteTimer = COYOTE_TIME; // Reset coyote timer when landing
                    hasDoubleJumped = false;
                } else if (velocityY < 0) {
                    // Moving up, hit ceiling
                    y = platform.getY() + platform.getHeight();
                    velocityY = 0;
                }

                playerBounds = getBounds(); // Update bounds after correction
            }
        }
    }

    /**
     * Take damage from an enemy or hazard.
     * 
     * @param amount Amount of damage to take
     */
    public void takeDamage(int amount) {
        if (!config.invincible) {
            health -= amount;
            if (health < 0)
                health = 0;
        }
    }

    /**
     * Heal the player.
     * 
     * @param amount Amount to heal
     */
    public void heal(int amount) {
        health += amount;
        if (health > maxHealth)
            health = maxHealth;
    }

    /**
     * Collect a coin.
     */
    public void collectCoin() {
        coins++;
    }

    /**
     * Bounce the player upward (e.g., when stomping an enemy).
     */
    public void bounce() {
        velocityY = -config.jumpStrength * 0.7;
        onGround = false;
    }

    /**
     * Reset the player to starting position.
     */
    public void reset() {
        x = config.playerStartX;
        y = config.playerStartY;
        velocityX = 0;
        velocityY = 0;
        onGround = false;
        coyoteTimer = 0;
        hasDoubleJumped = false;
    }

    /**
     * Get the collision bounds of the player.
     * 
     * @return Rectangle representing the player's hitbox
     */
    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, width, height);
    }

    /**
     * Draw the player.
     * 
     * @param g The graphics context
     */
    public void draw(Graphics g) {
        // Body
        g.setColor(color);
        g.fillRect((int) x, (int) y, width, height);

        // Outline
        g.setColor(color.darker());
        g.drawRect((int) x, (int) y, width, height);

        // Eyes (facing direction based on last movement)
        g.setColor(Color.WHITE);
        int eyeY = (int) y + 10;
        g.fillOval((int) x + 8, eyeY, 6, 6);
        g.fillOval((int) x + width - 14, eyeY, 6, 6);

        // Pupils
        g.setColor(Color.BLACK);
        g.fillOval((int) x + 10, eyeY + 2, 3, 3);
        g.fillOval((int) x + width - 12, eyeY + 2, 3, 3);
    }

    // Getters
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getHealth() {
        return health;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getCoins() {
        return coins;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public boolean isAlive() {
        return health > 0;
    }

    // Setters (for Week 11 encapsulation exercises)
    public void setHealth(int health) {
        this.health = Math.max(0, Math.min(health, maxHealth));
    }
}
