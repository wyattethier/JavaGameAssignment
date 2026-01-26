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
    public double x;
    public double y;

    // Velocity
    public double velocityX;
    public double velocityY;
    public double jumpMultiplier;
    public double moveSpeedMultiplier;

    // Size
    private int width;
    private int height;

    // State
    private boolean onGround;
    private int health;
    private int maxHealth;
    private int coins;
    private int stamina = 100; // Week 11: Objects & Classes

    // Coyote time - allows jumping shortly after leaving a platform
    private int coyoteTimer = 0;
    private final int COYOTE_TIME = 8; // frames of grace period after leaving ground
    private int iFrameTimer = 0; // Week 7: Invincibility frames
    private final int IFRAME_DURATION = 60; // 1 second of invincibility
    private int coinTimer = 0;
    private final int COIN_FRAMES = 15;

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
        this.jumpMultiplier = 1;
        this.moveSpeedMultiplier = 1;
        this.onGround = false;
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

        // Update invincibility timer
        if (iFrameTimer > 0) {
            iFrameTimer--;
        }

        // Update coin timer
        if (coinTimer > 0) {
            coinTimer--;
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
            velocityX = -config.determineMoveSpeed(moveSpeedMultiplier);
        } else if (input.isKeyPressed(KeyEvent.VK_RIGHT) || input.isKeyPressed(KeyEvent.VK_D)) {
            velocityX = config.determineMoveSpeed(moveSpeedMultiplier);
        } else {
            velocityX = 0;
        }

        // Jumping (with coyote time support)
        if (input.isKeyJustPressed(KeyEvent.VK_SPACE) || input.isKeyJustPressed(KeyEvent.VK_W)
                || input.isKeyJustPressed(KeyEvent.VK_UP)) {
            if (onGround || coyoteTimer > 0) {
                // Regular jump (includes coyote time window)
                velocityY = -config.calculateJumpHeight(jumpMultiplier);
                onGround = false;
                coyoteTimer = 0; // Consume the coyote time
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

                    // Support for moveable platforms (Week 11/12)
                    if (platform.isMoveable()) {
                        config.movePlatform(platform, -1);
                        y = platform.getY() - height; // Snap to new position
                    }
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
        // Only take damage if not in i-frames
        if (iFrameTimer <= 0) {
            health -= amount;
            if (health < 0)
                health = 0;

            // Activate i-frames
            iFrameTimer = IFRAME_DURATION;
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
        coinTimer = COIN_FRAMES;
        coins++;
    }

    /**
     * Bounce the player upward (e.g., when stomping an enemy).
     */
    public void bounce() {
        velocityY = -config.calculateJumpHeight(0.7);
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
        // Flickering effect during i-frames
        if (iFrameTimer > 0 && (iFrameTimer / 5) % 2 == 0) {
            return; // Skip drawing this frame to create flicker
        }

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

    public int getCoinTimer() {
        return coinTimer;
    }

    public int getStamina() {
        return stamina;
    }

    public double getJumpMultiplier() {
        return jumpMultiplier;
    }

    public double getMoveSpeedMultiplier() {
        return moveSpeedMultiplier;
    }

    // Setters (for Week 11 encapsulation exercises)
    public void setHealth(int health) {
        this.health = Math.max(0, Math.min(health, maxHealth));
    }

    public void setCoins(int coins) {
        this.coins = Math.max(0, coins);
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = Math.max(1, maxHealth);
    }

    public void setStamina(int stamina) {
        this.stamina = Math.max(0, Math.min(stamina, 100));
    }

    public void setJumpMultiplier(double jumpMultiplier) {
        this.jumpMultiplier = jumpMultiplier;
    }

    public void setMoveSpeedMultiplier(double moveSpeedMultiplier) {
        this.moveSpeedMultiplier = moveSpeedMultiplier;
    }
}
