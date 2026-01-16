package world;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import game.GameConfig;
import entities.Enemy;

/**
 * Represents a game level with platforms, enemies, and a goal.
 *
 * Students: You'll work with this class in Weeks 8-9 (arrays) and Week 10 (file
 * loading).
 */
public class Level {

    private String name;
    private String topic;
    private String hint;
    private Platform[] platforms;
    private Enemy[] enemies;
    private int goalX, goalY, goalWidth, goalHeight;
    private boolean goalEnabled; // Week 4: Can be disabled to teach if-statements
    private GameConfig config;

    // Encroaching wall (Week 3 mechanic)
    private boolean hasEncroachingWall;
    private double wallX;
    private double wallSpeed;
    private int wallStartDelay;
    private int wallDelayTimer;
    private Color wallColor;
    private boolean wallActive;

    // Level progression
    private int nextPart; // Next part after completing (0 = week complete)

    /**
     * Create a level from the game configuration.
     * The level layout is defined by arrays in GameConfig.
     *
     * @param config Game configuration containing level data
     */
    public Level(GameConfig config) {
        this.config = config;
        this.name = "Level 1";
        this.topic = "";
        this.hint = "";
        buildLevel();
    }

    /**
     * Create a level from LevelData configuration.
     * Used for the structured week-based levels.
     *
     * @param config    Game configuration for window size etc.
     * @param levelData The level-specific data
     */
    public Level(GameConfig config, LevelData.LevelConfig levelData) {
        this.config = config;
        this.name = levelData.name;
        this.topic = levelData.topic;
        this.hint = levelData.hint;
        buildLevelFromData(levelData);
    }

    /**
     * Build the level from the configuration arrays.
     * This is where the arrays from GameConfig are used!
     */
    private void buildLevel() {
        // Ground platform (always present)
        int groundPlatforms = 1;

        // Total platforms = ground + platforms from arrays
        int totalPlatforms = groundPlatforms + config.platformY.length;
        platforms = new Platform[totalPlatforms];

        // Create ground platform
        platforms[0] = new Platform(0, config.windowHeight - 50, config.windowWidth, 50, new Color(80, 60, 40));

        // ═══════════════════════════════════════════════════════════════
        // WEEK 8-9: This loop creates platforms from the arrays!
        // Modify the arrays in GameConfig to change the level layout.
        // ═══════════════════════════════════════════════════════════════
        for (int i = 0; i < config.platformY.length; i++) {
            int px = config.platformX[i];
            int py = config.platformY[i];
            int pw = config.platformWidths[i];

            platforms[groundPlatforms + i] = new Platform(px, py, pw, 20);
        }

        // ═══════════════════════════════════════════════════════════════
        // WEEK 5: This loop creates enemies!
        // Change config.enemyCount to spawn more or fewer enemies.
        // ═══════════════════════════════════════════════════════════════
        enemies = new Enemy[config.enemyCount];
        for (int i = 0; i < config.enemyCount; i++) {
            // Use positions from arrays if available, otherwise use defaults
            double ex = (i < config.enemyPositions.length) ? config.enemyPositions[i] : 200 + (i * 200);
            double ey = (i < config.enemyY.length) ? config.enemyY[i] : config.windowHeight - 90;

            // Set patrol bounds based on nearby platforms
            double patrolLeft = Math.max(0, ex - 80);
            double patrolRight = Math.min(config.windowWidth, ex + 120);

            enemies[i] = new Enemy(ex, ey, patrolLeft, patrolRight);
        }

        goalX = config.goalX;
        goalY = config.goalY;
        goalWidth = config.goalWidth;
        goalHeight = config.goalHeight;
        goalEnabled = true; // Default levels have goal enabled

        // No wall by default
        hasEncroachingWall = false;
    }

    /**
     * Build the level from LevelData configuration.
     */
    private void buildLevelFromData(LevelData.LevelConfig data) {
        // Ground platform (always present)
        int groundPlatforms = 1;

        // Total platforms = ground + platforms from data
        int totalPlatforms = groundPlatforms + data.platformY.length;
        platforms = new Platform[totalPlatforms];

        // Create ground platform
        platforms[0] = new Platform(0, config.windowHeight - 50, config.windowWidth, 50, new Color(80, 60, 40));

        // Create platforms from level data arrays
        for (int i = 0; i < data.platformY.length; i++) {
            int px = data.platformX[i];
            int py = data.platformY[i];
            int pw = data.platformWidths[i];

            platforms[groundPlatforms + i] = new Platform(px, py, pw, 20);
        }

        // Create enemies from level data
        enemies = new Enemy[data.enemyX.length];
        for (int i = 0; i < data.enemyX.length; i++) {
            double ex = data.enemyX[i];
            double ey = data.enemyY[i];
            double patrolLeft = Math.max(0, ex - 80);
            double patrolRight = Math.min(config.windowWidth, ex + 120);
            enemies[i] = new Enemy(ex, ey, patrolLeft, patrolRight);
        }

        // Goal position
        goalX = data.goalX;
        goalY = data.goalY;
        goalWidth = config.goalWidth;
        goalHeight = config.goalHeight;

        // Week 4: Read goalEnabled from GameConfig so students can modify it!
        // Other weeks use the level data's default (usually true)
        if (data.weekNumber == 4) {
            goalEnabled = config.goalEnabled; // Students change this in GameConfig.java!
        } else {
            goalEnabled = data.goalEnabled;
        }

        // Encroaching wall setup
        hasEncroachingWall = data.hasEncroachingWall;
        wallX = data.wallStartX;
        wallSpeed = data.wallSpeed;
        wallStartDelay = data.wallStartDelay;
        wallDelayTimer = wallStartDelay;
        wallColor = data.wallColor;
        wallActive = false;

        // Level progression
        nextPart = data.nextPart;
    }

    /**
     * Update all level elements.
     */
    public void update() {
        // Update enemies
        for (Enemy enemy : enemies) {
            if (enemy != null) {
                enemy.update();
            }
        }

        // Update encroaching wall
        if (hasEncroachingWall) {
            if (wallDelayTimer > 0) {
                wallDelayTimer--;
            } else {
                wallActive = true;
                wallX += wallSpeed;
            }
        }
    }

    /**
     * Check if the player is touching the encroaching wall.
     * 
     * @param playerX     Player's X position
     * @param playerWidth Player's width
     * @return true if player is caught by the wall
     */
    public boolean isPlayerCaughtByWall(double playerX, int playerWidth) {
        if (!hasEncroachingWall || !wallActive) {
            return false;
        }
        return playerX < wallX;
    }

    /**
     * Get the current wall X position (for UI display).
     */
    public double getWallX() {
        return wallX;
    }

    /**
     * Check if this level has an encroaching wall.
     */
    public boolean hasWall() {
        return hasEncroachingWall;
    }

    /**
     * Check if the wall is currently active (moving).
     */
    public boolean isWallActive() {
        return wallActive;
    }

    /**
     * Get the wall delay timer (for countdown display).
     */
    public int getWallDelayTimer() {
        return wallDelayTimer;
    }

    /**
     * Check if a position is at the goal.
     * 
     * @param x      X position to check
     * @param y      Y position to check
     * @param width  Width of object
     * @param height Height of object
     * @return true if overlapping with goal
     */
    public boolean isAtGoal(double x, double y, int width, int height) {
        // ═══════════════════════════════════════════════════════════════
        // WEEK 4: The goal only works if goalEnabled is true!
        // If the flag isn't working, check GameConfig.java goalEnabled
        // ═══════════════════════════════════════════════════════════════
        if (goalEnabled == false) {
            return false; // Goal is broken/disabled!
        }

        Rectangle goalBounds = new Rectangle(goalX, goalY, goalWidth, goalHeight);
        Rectangle checkBounds = new Rectangle((int) x, (int) y, width, height);
        return goalBounds.intersects(checkBounds);
    }

    /**
     * Draw the level (platforms and goal).
     * 
     * @param g The graphics context
     */
    public void draw(Graphics g) {
        // Draw encroaching wall first (behind everything)
        if (hasEncroachingWall) {
            drawEncroachingWall(g);
        }

        // Draw platforms
        for (Platform platform : platforms) {
            if (platform != null) {
                platform.draw(g);
            }
        }

        // Draw goal (flag/door)
        drawGoal(g);

        // Draw enemies
        for (Enemy enemy : enemies) {
            if (enemy != null) {
                enemy.draw(g);
            }
        }
    }

    /**
     * Draw the encroaching wall.
     */
    private void drawEncroachingWall(Graphics g) {
        // Draw the deadly wall
        g.setColor(wallColor);
        int wallWidth = (int) Math.max(0, wallX + 100);
        g.fillRect(0, 0, wallWidth, config.windowHeight);

        // Draw warning edge
        if (wallActive) {
            g.setColor(new Color(255, 50, 50));
            g.fillRect((int) wallX - 5, 0, 10, config.windowHeight);

            // Animated warning stripes
            g.setColor(new Color(255, 200, 0));
            for (int y = (int) (System.currentTimeMillis() / 50 % 40) - 40; y < config.windowHeight; y += 40) {
                g.fillRect((int) wallX - 3, y, 6, 20);
            }
        }

        // Warning countdown if wall hasn't started yet
        if (wallDelayTimer > 0) {
            g.setColor(new Color(255, 100, 100, 200));
            g.setFont(new java.awt.Font("Arial", java.awt.Font.BOLD, 24));
            int seconds = (wallDelayTimer / 60) + 1;
            g.drawString("WALL IN: " + seconds, 20, 120);
        }
    }

    /**
     * Draw the goal area.
     */
    private void drawGoal(Graphics g) {
        // Flag pole
        g.setColor(new Color(139, 90, 43));
        g.fillRect(goalX + goalWidth / 2 - 3, goalY, 6, goalHeight + 30);

        // Flag color depends on whether goal is enabled
        if (goalEnabled) {
            g.setColor(new Color(50, 205, 50)); // Lime green = working!
        } else {
            g.setColor(new Color(128, 128, 128)); // Gray = broken!
        }
        int[] xPoints = { goalX + goalWidth / 2 + 3, goalX + goalWidth / 2 + 40, goalX + goalWidth / 2 + 3 };
        int[] yPoints = { goalY, goalY + 15, goalY + 30 };
        g.fillPolygon(xPoints, yPoints, 3);

        // Flag border
        if (goalEnabled) {
            g.setColor(new Color(34, 139, 34)); // Dark green
        } else {
            g.setColor(new Color(80, 80, 80)); // Dark gray
        }
        g.drawPolygon(xPoints, yPoints, 3);

        // "GOAL" text or "BROKEN" text
        g.setColor(Color.WHITE);
        if (goalEnabled) {
            g.drawString("GOAL", goalX + 5, goalY - 5);
        } else {
            g.setColor(new Color(255, 100, 100));
            g.drawString("BROKEN!", goalX - 5, goalY - 5);
        }
    }

    /**
     * Reset the level (respawn enemies, etc.)
     */
    public void reset() {
        // Reset wall position if applicable
        if (hasEncroachingWall) {
            wallX = -50;
            wallDelayTimer = wallStartDelay;
            wallActive = false;
        }

        // Rebuild platforms and enemies
        if (topic != null && !topic.isEmpty()) {
            // Was built from level data - need to rebuild properly
            LevelData.LevelConfig data = LevelData.getLevel(3); // Week 3 for now
            buildLevelFromData(data);
        } else {
            buildLevel();
        }
    }

    // Getters
    public Platform[] getPlatforms() {
        return platforms;
    }

    public Enemy[] getEnemies() {
        return enemies;
    }

    public String getName() {
        return name;
    }

    public String getTopic() {
        return topic;
    }

    public String getHint() {
        return hint;
    }

    public int getNextPart() {
        return nextPart;
    }
}
