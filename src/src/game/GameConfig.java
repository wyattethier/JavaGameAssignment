package game;

import world.Platform;
import entities.Enemy;
import java.io.File;
import java.util.Scanner;

/**
 * ========================================================
 * STUDENT MODIFICATION ZONE
 * ========================================================
 *
 * Welcome! This is where you'll change the game.
 * Each section corresponds to different weeks in the course.
 *
 * INSTRUCTIONS:
 * 1. Find the section for your current week
 * 2. Read the comments to understand each variable
 * 3. Change the values and run the game to see the effect
 * 4. Experiment! You can't break anything permanently.
 *
 * ========================================================
 */
public class GameConfig extends BaseSettings {

    // ╔═══════════════════════════════════════════════════════════════╗
    // ║ LEVEL SELECTOR ║
    // ╠═══════════════════════════════════════════════════════════════╣
    // ║ Change this to play different weeks' levels (3-14) ║
    // ╚═══════════════════════════════════════════════════════════════╝

    // Which week's level do you want to play? (3-14)
    // Week 3 = Data Types challenge (jump height + speed escape)
    // Week 4 = Decisions (coming soon)
    // ... more levels coming each week!
    public int currentWeek = 3;

    // ╔═══════════════════════════════════════════════════════════════╗
    // ║ SECTION 1: PLAYER PHYSICS (Weeks 2-3: Data Types) ║
    // ╠═══════════════════════════════════════════════════════════════╣
    // ║ Modify these values to change how the player moves and jumps ║
    // ╚═══════════════════════════════════════════════════════════════╝

    // How high does the player jump?
    // This is the initial upward velocity when you press jump
    public double jumpStrength = 11.0;

    // How fast does the player move left/right?
    public double moveSpeed = 3.0;

    // How fast does the player fall?
    // Lower = floatier jumps, Higher = heavier falls
    public double gravity = 0.5;

    // Your player's name (shown in the game)
    public String playerName = "Hero";

    // Player size
    public int playerWidth = 32;
    public int playerHeight = 48;

    // ═══════════════════════════════════════════════════════════════
    // WEEK 4 CHALLENGE: Fix the broken goal!
    // Write an if-statement that enables the goal only after the
    // button has been clicked enough times.
    // ═══════════════════════════════════════════════════════════════

    /**
     * This method is called every frame to check if the goal is fixed.
     * Use the constant: REQUIRED_BUTTON_CLICKS for the conditional
     * 
     * @param clickCount How many times the button was clicked
     * @return true if the goal should be active, false otherwise
     */
    public boolean isGoalFixed(int clickCount) {
        // Write an if-statement that returns true if the player has
        // clicked the button at least REQUIRED_BUTTON_CLICKS times.

        return false;
    }

    // ╔═══════════════════════════════════════════════════════════════╗
    // ║ SECTION 3: SPAWNING (Week 5: Loops) ║
    // ╠═══════════════════════════════════════════════════════════════╣
    // ║ Change these to control how many things appear in the level ║
    // ║ These values are used in for-loops to spawn objects ║
    // ╚═══════════════════════════════════════════════════════════════╝

    // How many extra platforms appear in the level? (Try 0-10)
    public int extraPlatformCount = 3;

    // How many enemies patrol the level? (Try 0-5)
    public int enemyCount = 2;

    // How many coins to collect? (Try 5-20)
    public int coinCount = 10;

    // ═══════════════════════════════════════════════════════════════
    // WEEK 5 CHALLENGE: Build the Bridge!
    // The bridge has NUM_BRIDGE_TILES tiles, but they are all inactive.
    // Write a for-loop that activates them all by setting each element
    // in the array to true.
    // ═══════════════════════════════════════════════════════════════

    /**
     * This method is called once to set up the bridge.
     * Use a for-loop to set each element of the 'tiles' array to true.
     * 
     * @param tiles An array of booleans, one for each bridge tile.
     */
    public void activateBridgeTiles(boolean[] tiles) {
        // Write a for-loop that sets all tiles in the array to true.

        // For now, the bridge remains broken (all tiles are false)
    }

    // ╔═══════════════════════════════════════════════════════════════╗
    // ║ SECTION 4: LEVEL LAYOUT (Weeks 8-9: Arrays) ║
    // ╠═══════════════════════════════════════════════════════════════╣
    // ║ Arrays that define where things are placed in the level ║
    // ║ Each index (0, 1, 2...) is a different platform or enemy ║
    // ╚═══════════════════════════════════════════════════════════════╝

    // Platform Y positions (height from top of screen)
    // Lower numbers = higher on screen
    public int[] platformY = { 450, 350, 250, 180, 120 };

    // Platform X positions (distance from left edge)
    public int[] platformX = { 50, 200, 400, 550, 300 };

    // Platform widths
    public int[] platformWidths = { 120, 100, 100, 120, 150 };

    // Should each platform move when touched? (Week 11/12)
    public boolean[] platformMoveable = { false, false, false, false, true };

    // Maximum height for moveable platforms (-1 = no limit)
    public int[] platformMaxHeights = { -1, -1, -1, -1, 50 };

    // Enemy X positions (where enemies spawn)
    public int[] enemyPositions = { 250, 500 };

    // Enemy Y positions (what platform height they're on)
    public int[] enemyY = { 402, 302 };

    // ═══════════════════════════════════════════════════════════════
    // SECTION 5: CHALLENGES (Weeks 6-14)
    // Write your code in the methods below!
    // ═══════════════════════════════════════════════════════════════

    /**
     * WEEK 6 CHALLENGE: Activate the Jump Pad!
     * Call the calculateJumpHeight method with a 2.5 multiplier.
     */
    public double getJumpPadBoost() {

        return 0;
    }

    /**
     * WEEK 6 CHALLENGE: Jump Pad Activation Condition
     * The pad should only work if the player has collected at least 3 coins.
     */
    public boolean isJumpPadActive(int playerCoins) {

        return false;
    }

    /**
     * WEEK 7 CHALLENGE: Calculate the player's total score.
     * FORMULA: (coins * 10 + timeBonus) * comboMultiplier
     */
    public int calculateScore(int coins, int timeBonus, double comboMultiplier) {
        return 0;
    }

    /**
     * WEEK 7 CHALLENGE: Calculate trap damage.
     * FORMULA: Math.round(baseDamage * (1 - armorPercent/100.0))
     * Make sure the damage is at least 1.
     */
    public int calculateDamage(int baseDamage, int armorPercent) {
        return 3;
    }

    // ╔═══════════════════════════════════════════════════════════════╗
    // ║ SECTION 6: ADVANCED LAYOUTS (Weeks 8-9) ║
    // ╠═══════════════════════════════════════════════════════════════╣
    // ║ These arrays are used for the Week 8 & 9 challenges ║
    // ╚═══════════════════════════════════════════════════════════════╝

    /**
     * WEEK 8 CHALLENGE:
     * You need to use the movePlatform method to raise the player up to reach the
     * flag!
     * 
     * The method is called when the player touches the platform
     * use PLATFORM_VELOCITY to control the speed
     * use platform.getY() and platform.setY(int y) to move the platform
     * use platform.getMaxHeight() to limit the platform's movement
     */

    public void movePlatform(Platform platform, int dy) {
        // TODO: Move the platform!
        // make sure the platform doesn't move past maxHeight

    }

    // Week 9: Enemy configuration
    public int[] enemyStartX = { 200, 300, 400, 500, 600, 600, 600, 600, 600, 600 };
    public int[] enemyStartY = { 450, 450, 450, 450, 450, 450, 450, 450, 450, 450 };
    public int[] enemyPatrolLeft = { 40, 100, 160, 220, 280, 340, 400, 460, 520, 580 };
    public int[] enemyPatrolRight = { 100, 160, 220, 280, 340, 400, 460, 520, 580, 640 };

    /**
     * WEEK 9 CHALLENGE: Use loops to configure the enemies.
     * 
     * @return An array of configured enemies
     *         Jump on the enemies to get across the void!
     */
    public Enemy[] configureEnemies() {
        return null;
    }

    // ╔═══════════════════════════════════════════════════════════════╗
    // ║ SECTION 7: OBJECTS & CLASSES (Weeks 11-14) ║
    // ╠═══════════════════════════════════════════════════════════════╣
    // ║ Challenges for the final weeks of the course ║
    // ╚═══════════════════════════════════════════════════════════════╝
    /**
     * WEEK 10 CHALLENGE: Load level platforms from a file.
     * Review level10.txt and write the method accordingly.
     * Platforms are created with parameters: (x, y, width, height)
     * and the txt file has them listed x, y, width (no height is needed).
     * Return a 2d array of platforms with the x, y, and width values.
     */
    public int[][] loadLevelFromFile(String filename) {
        return null;
    }

    /**
     * WEEK 10 CHALLENGE: Provide a fallback level.
     */
    public int[][] createFallbackLevel() {
        // TODO: Return default platforms in a 2D array
        return null;
    }

    /**
     * WEEK 11 CHALLENGE: Stat Gate check.
     * Return true if health is REQUIRED_HEALTH and coins >= REQUIRED_COINS.
     */
    public boolean canPassStatGate(entities.Player player) {
        // TODO: Use getters to check stats
        return false;
    }

    /**
     * WEEK 11 CHALLENGE: Initial player setup.
     */
    public void configurePlayer(entities.Player player) {
        // TODO: Use setters to set health and coins
        // You may notice the player's max health isn't high enough
    }

    /**
     * WEEK 12 CHALLENGE: Implement the PowerUp class.
     * Navigate to the PowerUp class and add the necessary code.
     * Add your code to the apply method to give the player abilities.
     */

    /**
     * WEEK 13 CHALLENGE: Fix the GameState class.
     * Navigate to the GameState class and follow the instructions.
     */

    /**
     * WEEK 14 CHALLENGE: Create a new enemy subclass.
     * Review the Charger, and Flyer classes for inspiration.
     * Make sure your new enemy inherits from the Enemy class.
     * 
     * Use the code below to create your new enemy:
     * 
     * public *your Enemy Subclass* createNewEnemy() {
     * return new *your Enemy Subclass*(x, y, patrolLeft, patrolRight);
     * }
     * 
     * (Recommended x, y, and patrol values are present but you can change those).
     */

    public Enemy createNewEnemy() {
        return new Enemy(100, 440, 50, 250);
    }

    // Goal position (where the level ends)
    public int goalX = 700;
    public int goalY = 72;
}
