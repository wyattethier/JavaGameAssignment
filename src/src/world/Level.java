package world;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import game.GameConfig;
import entities.Enemy;
import entities.LevelButton;
import entities.JumpPad;
import entities.ScoringDoor;
import entities.Trap;
import entities.StatGate;
import entities.PowerUp;
import entities.Coin;

import interfaces.Constants;

/**
 * Represents a game level with platforms, enemies, and a goal.
 *
 * Students: You'll work with this class in Weeks 8-9 (arrays) and Week 10 (file
 * loading).
 */
public class Level implements Constants {

    private String name;
    private String topic;
    private String hint;
    private Platform[] platforms;
    private Enemy[] enemies;
    private Coin[] coins;
    private int goalX, goalY, goalWidth, goalHeight;
    private boolean goalEnabled; // Week 4: Can be disabled to teach if-statements
    private GameConfig config;
    private LevelButton button; // Week 4 click challenge

    // Specialized Entities
    private JumpPad jumpPad; // Week 6
    private ScoringDoor scoringDoor; // Week 7
    private Trap trap; // Week 7
    private StatGate statGate; // Week 11
    private java.util.ArrayList<PowerUp> powerUps; // Week 12

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

    // Bridge (Week 5 mechanic)
    private Platform[] bridgeTiles;
    private boolean[] bridgeActive;
    private boolean hasBridge;

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
            boolean pm = (config.platformMoveable != null && i < config.platformMoveable.length)
                    ? config.platformMoveable[i]
                    : false;
            int mh = (config.platformMaxHeights != null && i < config.platformMaxHeights.length)
                    ? config.platformMaxHeights[i]
                    : -1;

            platforms[groundPlatforms + i] = new Platform(px, py, pw, 20, new Color(139, 90, 43), pm, mh);
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

        // ═══════════════════════════════════════════════════════════════
        // WEEK 5: This loop creates coins!
        // Change config.coinCount to spawn more or fewer coins.
        // ═══════════════════════════════════════════════════════════════
        /*
         * if (config.coinX != null && config.coinX.length > 0) {
         * coins = new Coin[config.coinX.length];
         * for (int i = 0; i < config.coinX.length; i++) {
         * coins[i] = new Coin(config.coinX[i], config.coinY[i]);
         * }
         * } else {
         * coins = new Coin[config.coinCount];
         * for (int i = 0; i < config.coinCount; i++) {
         * // Scatter coins across platforms
         * int platformIndex = i % platforms.length;
         * int cx = platforms[platformIndex].getX() +
         * (platforms[platformIndex].getWidth() / 2);
         * int cy = platforms[platformIndex].getY() - 30;
         * coins[i] = new Coin(cx, cy);
         * }
         * }
         */

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
        // Ground platform (conditionally present)
        int groundPlatforms = data.hasFloor ? 1 : 0;

        // Total platforms = ground + platforms from data
        int totalPlatforms = groundPlatforms + data.platformY.length;
        platforms = new Platform[totalPlatforms];

        // Create ground platform if enabled
        if (data.hasFloor) {
            platforms[0] = new Platform(0, config.windowHeight - 50, config.windowWidth, 50, new Color(80, 60, 40));
        }

        // Create platforms from level data arrays
        for (int i = 0; i < data.platformY.length; i++) {
            int px = data.platformX[i];
            int py = data.platformY[i];
            int pw = data.platformWidths[i];
            boolean pm = (data.platformMoveable != null && i < data.platformMoveable.length)
                    ? data.platformMoveable[i]
                    : false;
            int mh = (data.platformMaxHeights != null && i < data.platformMaxHeights.length)
                    ? data.platformMaxHeights[i]
                    : -1;

            platforms[groundPlatforms + i] = new Platform(px, py, pw, 20, new Color(139, 90, 43), pm, mh);
        }

        // Create enemies from level data
        if (data.useWeek9Data) {
            this.enemies = config.configureEnemies();
        } else {
            enemies = new Enemy[data.enemyX.length];
            for (int i = 0; i < data.enemyX.length; i++) {
                double ex = data.enemyX[i];
                double ey = data.enemyY[i];
                double patrolLeft = Math.max(0, ex - 80);
                double patrolRight = Math.min(config.windowWidth, ex + 120);
                enemies[i] = new Enemy(ex, ey, patrolLeft, patrolRight);
            }
        }

        // Create coins from level data
        coins = new Coin[data.coinX.length];
        for (int i = 0; i < data.coinX.length; i++) {
            coins[i] = new Coin(data.coinX[i], data.coinY[i]);
        }

        // Goal position
        goalX = data.goalX;
        goalY = data.goalY;
        goalWidth = config.goalWidth;
        goalHeight = config.goalHeight;

        // Encroaching wall setup
        hasEncroachingWall = data.hasEncroachingWall;
        wallX = data.wallStartX;
        wallSpeed = data.wallSpeed;
        wallStartDelay = data.wallStartDelay;
        wallDelayTimer = wallStartDelay;
        wallColor = data.wallColor;
        wallActive = false;

        // Button setup (Week 4)
        if (data.hasLevelButton) {
            button = new LevelButton(data.buttonX, data.buttonY, data.buttonWidth, data.buttonHeight, config);
        } else {
            button = null;
        }

        // Level progression
        nextPart = data.nextPart;

        // Bridge setup (Week 5)
        hasBridge = data.hasBridge;
        if (hasBridge) {
            bridgeTiles = new Platform[NUM_BRIDGE_TILES];
            bridgeActive = new boolean[NUM_BRIDGE_TILES];

            // Initialize bridge tiles (initially all inactive)
            for (int i = 0; i < NUM_BRIDGE_TILES; i++) {
                int tx = data.bridgeStartX + (i * data.bridgeTileWidth);
                bridgeTiles[i] = new Platform(tx, data.bridgeY, data.bridgeTileWidth, 20,
                        new Color(100, 100, 100, 100));
                bridgeActive[i] = false;
            }

            // Call student's loop to activate tiles
            config.activateBridgeTiles(bridgeActive);
        }

        // Week 4 specific: Goal starts disabled (student must fix it)
        // All other weeks: Goal starts enabled
        goalEnabled = (config.currentWeek != 4);

        // Week 6: Jump Pad
        if (data.hasJumpPad) {
            jumpPad = new JumpPad(data.jumpPadX, data.jumpPadY, 60, 20, config);
        } else {
            jumpPad = null;
        }

        // Week 7: Scoring Door & Trap
        if (data.hasScoringDoor) {
            scoringDoor = new ScoringDoor(data.scoringDoorX, data.scoringDoorY, 40, 120, config);
        } else {
            scoringDoor = null;
        }

        if (data.hasTrap) {
            trap = new Trap(data.trapX, data.trapY, data.trapWidth, 20, 1, config);
        } else {
            trap = null;
        }

        // Week 11: Stat Gate
        if (data.hasStatGate) {
            statGate = new StatGate(data.statGateX, data.statGateY, 20, 140, config);
        } else {
            statGate = null;
        }

        // Week 12: Power Ups
        powerUps = new java.util.ArrayList<>();
        if (config.currentWeek == 12) {
            // Add some practice powerups
            powerUps.add(new PowerUp("speed", 300, 450));
            powerUps.add(new PowerUp("jump", 500, 450));
        }

        // Week 10: File Loading
        if (data.useFileLoading) {
            loadLevelFromFile(data.levelFileName);
        }
    }

    private void loadLevelFromFile(String filename) {
        // This is where the student would implement file loading in Week 10.
        // For now, we'll try to call their method in GameConfig.
        int[][] result = config.loadLevelFromFile(filename);
        if (result == null) {
            result = config.createFallbackLevel();
        }

        if (result != null && result.length >= 3) {
            int count = result[0].length;
            platforms = new Platform[count];
            for (int i = 0; i < count; i++) {
                platforms[i] = new Platform(result[0][i], result[1][i], result[2][i], 20);
            }
        }
    }

    /**
     * Week 14: Special setup for polymorphic enemies.
     */
    public void setupWeek14Enemies(entities.Player player) {
        enemies = new Enemy[3];
        enemies[0] = new Enemy(100, 440, 50, 250); // Standard
        enemies[1] = new entities.Flyer(300, 300, 200, 400); // Flyer
        enemies[2] = new entities.Charger(550, 320, 500, 700, player); // Charger
    }

    /**
     * Check collisions between player and specialized entities.
     */
    public String checkCollisions(entities.Player player, int timeBonus, double comboMultiplier) {
        Rectangle pb = player.getBounds();

        // Coins
        if (coins != null && player.getCoinTimer() <= 0) {
            for (Coin coin : coins) {
                if (!coin.isCollected() && coin.checkCollision(pb)) {
                    player.collectCoin();
                    return "COIN";
                }
            }
        }

        // Power Ups
        if (powerUps != null) {
            for (PowerUp pu : powerUps) {
                if (!pu.isCollected() && pb.intersects(pu.getBounds())) {
                    pu.setCollected(true);
                    pu.apply(player);
                    return "POWERUP";
                }
            }
        }

        // Jump Pad
        if (jumpPad != null && pb.intersects(jumpPad.getBounds())) {
            if (config.isJumpPadActive(player.getCoins())) {
                player.velocityY = -config.getJumpPadBoost();
                return "BOOSTED";
            }
        }

        // Trap
        if (trap != null && pb.intersects(trap.getBounds())) {
            player.takeDamage(trap.getDamage(Constants.ARMOR_PERCENT)); // 0 armor for now
            return "DAMAGED";
        }

        // Scoring Door
        if (scoringDoor != null && pb.intersects(scoringDoor.getBounds())) {
            if (!scoringDoor.isOpen(player.getCoins(), timeBonus, comboMultiplier)) {
                // Push player back
                player.x = scoringDoor.getBounds().x - player.getWidth();
                return "BLOCKED";
            }
        }

        // Stat Gate
        if (statGate != null && pb.intersects(statGate.getBounds())) {
            if (!config.canPassStatGate(player)) {
                player.x = statGate.getBounds().x - player.getWidth();
                return "GATED";
            }
        }

        // Platform
        pb = player.getBounds();
        for (Platform platform : getPlatforms()) {
            if (platform != null && pb.intersects(platform.getBounds())) {
                player.y = platform.getBounds().y - player.getHeight();
                player.velocityY = 0;
                return "PLATFORM";
            }
        }

        return null;
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
        if (!goalEnabled) {
            return false;
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

        // Draw button if present
        if (button != null) {
            button.draw(g);
        }

        // Draw bridge if present
        if (hasBridge) {
            for (int i = 0; i < bridgeTiles.length; i++) {
                Platform t = bridgeTiles[i];
                if (bridgeActive[i]) {
                    // Solid bridge tile
                    g.setColor(new Color(139, 69, 19)); // Saddle Brown
                    g.fillRect(t.getX(), t.getY(), t.getWidth(), t.getHeight());
                    g.setColor(new Color(101, 67, 33)); // Darker brown border
                    g.drawRect(t.getX(), t.getY(), t.getWidth(), t.getHeight());
                } else {
                    // Ghost/Inactive bridge tile
                    g.setColor(new Color(200, 200, 200, 80));
                    g.drawRect(t.getX(), t.getY(), t.getWidth(), t.getHeight());
                    g.setFont(new java.awt.Font("Arial", java.awt.Font.ITALIC, 10));
                    g.drawString("INACTIVE", t.getX() + 5, t.getY() + 15);
                }
            }
        }

        // Draw enemies
        for (Enemy enemy : enemies) {
            if (enemy != null) {
                enemy.draw(g);
            }
        }

        // Draw Specialized Entities
        if (jumpPad != null) {
            // Need player coins for activation check
            // Pass 0 if we don't have access to player here?
            // Level doesn't track player. We'll handle this in draw special call.
        }

        if (scoringDoor != null) {
            // Need scores...
        }

        if (trap != null) {
            trap.draw(g);
        }

        if (statGate != null) {
            // Need player...
        }

        if (powerUps != null) {
            for (PowerUp pu : powerUps) {
                pu.draw(g);
            }
        }

        // Draw coins
        if (coins != null) {
            for (Coin coin : coins) {
                if (coin != null) {
                    coin.draw(g);
                }
            }
        }
    }

    /**
     * Draw specialized entities that require player state.
     */
    public void drawSpecial(Graphics g, entities.Player player, int timeBonus, double comboMultiplier) {
        if (jumpPad != null) {
            jumpPad.draw(g, player.getCoins());
        }
        if (scoringDoor != null) {
            scoringDoor.draw(g, player.getCoins(), timeBonus, comboMultiplier);
        }
        if (statGate != null) {
            statGate.draw(g, player);
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
            LevelData.LevelConfig data = LevelData.getLevel(config.currentWeek);
            buildLevelFromData(data);
        } else {
            buildLevel();
        }

        // Reset button
        if (button != null) {
            button.reset();
        }
    }

    // Getters
    public Platform[] getPlatforms() {
        if (!hasBridge) {
            return platforms;
        }

        // Combine permanent platforms with active bridge tiles
        int activeBridgeCount = 0;
        for (boolean active : bridgeActive) {
            if (active)
                activeBridgeCount++;
        }

        Platform[] allPlatforms = new Platform[platforms.length + activeBridgeCount];
        System.arraycopy(platforms, 0, allPlatforms, 0, platforms.length);

        int index = platforms.length;
        for (int i = 0; i < bridgeActive.length; i++) {
            if (bridgeActive[i]) {
                allPlatforms[index++] = bridgeTiles[i];
            }
        }
        return allPlatforms;
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

    /**
     * Handle mouse click event.
     */
    public void handleMouseClick(int mouseX, int mouseY) {
        if (button != null) {
            button.handleClick(mouseX, mouseY);
        }
    }

    public LevelButton getButton() {
        return button;
    }

    public void setGoalEnabled(boolean enabled) {
        this.goalEnabled = enabled;
    }
}
